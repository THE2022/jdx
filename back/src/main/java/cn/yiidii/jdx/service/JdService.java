package cn.yiidii.jdx.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.jdx.model.dto.JdInfo;
import cn.yiidii.jdx.model.ex.BizException;
import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * JdService
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class JdService {

    TimedCache<String, JdInfo> timedCache = CacheUtil.newTimedCache(2 * 60 * 1000);

    private static final String APP_ID = "959";
    private static final String Q_VERSION = "1.0.0";
    private static final String COUNTRY_CODE = "86";

    public JdInfo sendSmsCode(String mobile) throws Exception {
        String subCmd = "1";
        long timestamp = System.currentTimeMillis();

        // 第一步，获取一堆什么参数
        String sign = DigestUtil.md5Hex(StrUtil.format("{}{}{}36{}sb2cwlYyaCSN1KUv5RHG3tmqxfEb8NKN", APP_ID, Q_VERSION, timestamp, subCmd));
        String param = StrUtil.format("client_ver=1.0.0&gsign={}&appid={}&return_page=https%3A%2F%2Fcrpl.jd.com%2Fn%2Fmine%3FpartnerId%3DWBTF0KYY%26ADTAG%3Dkyy_mrqd%26token%3D&cmd=36&sdk_ver=1.0.0&sub_cmd=1&qversion={}&ts={}", sign, APP_ID, Q_VERSION, timestamp);
        HttpResponse response = HttpRequest.post("https://qapplogin.m.jd.com/cgi-bin/qapp/quick")
                .body(param, ContentType.FORM_URLENCODED.toString())
                .execute();
        JSONObject responseJo = JSONObject.parseObject(response.body());
        this.checkErr(responseJo);
        JSONObject data = responseJo.getJSONObject("data");
        String gsalt = data.getString("gsalt");
        String guid = data.getString("guid");
        String lsid = data.getString("lsid");
        String rsaModulus = data.getString("rsa_modulus");
        String ck = StrUtil.format("guid={}; lsid={}; gsalt={};rsa_modulus={};", guid, lsid, gsalt, rsaModulus);
        JdInfo jdInfo = JdInfo.builder()
                .gsalt(gsalt)
                .guid(guid)
                .lsId(lsid)
                .gsalt(gsalt)
                .rsaModulus(rsaModulus)
                .preCookie(ck)
                .build();

        // 第二步，发送验证码
        timestamp = System.currentTimeMillis();
        subCmd = "2";
        String gsign = DigestUtil.md5Hex(StrUtil.format("{}{}{}36{}{}", APP_ID, Q_VERSION, timestamp, subCmd, gsalt));
        sign = DigestUtil.md5Hex(StrUtil.format("{}{}{}{}4dtyyzKF3w6o54fJZnmeW3bVHl0$PbXj", APP_ID, Q_VERSION, COUNTRY_CODE, mobile));
        param = StrUtil.format("country_code={}&client_ver=1.0.0&gsign={}&appid={}&mobile={}&sign={}&cmd=36&sub_cmd={}&qversion={}&ts={}", COUNTRY_CODE, gsign, APP_ID, mobile, sign, subCmd, Q_VERSION, timestamp);

        response = HttpRequest.post("https://qapplogin.m.jd.com/cgi-bin/qapp/quick")
                .body(param, ContentType.FORM_URLENCODED.toString())
                .cookie(ck)
                .execute();
        responseJo = JSONObject.parseObject(response.body());
        this.checkErr(responseJo);
        timedCache.put(mobile, jdInfo);
        return jdInfo;
    }

    public JdInfo login(String mobile, String code) throws Exception {
        JdInfo jdInfo = timedCache.get(mobile);
        if (Objects.isNull(jdInfo)) {
            throw new BizException("请先获取验证码");
        }

        String subCmd = "3";
        long timestamp = System.currentTimeMillis();
        String gsign = StrUtil.format("{}{}{}36{}{}", APP_ID, Q_VERSION, timestamp, subCmd, jdInfo.getGsalt());
        String param = StrUtil.format("country_code={}&client_ver=1.0.0&gsign={}&smscode={}&appid={}&mobile={}&cmd=36&sub_cmd={}&qversion={}&ts={}", COUNTRY_CODE, gsign, code, APP_ID, mobile, subCmd, Q_VERSION, timestamp);
        HttpResponse response = HttpRequest.post("https://qapplogin.m.jd.com/cgi-bin/qapp/quick")
                .body(param, ContentType.FORM_URLENCODED.toString())
                .cookie(jdInfo.getPreCookie())
                .execute();
        JSONObject responseJo = JSONObject.parseObject(response.body());
        this.checkErr(responseJo);
        JSONObject data = responseJo.getJSONObject("data");
        String ptKey = data.getString("pt_key");
        String ptPin = data.getString("pt_pin");
        String cookie = StrUtil.format("pt_key={};pt_pin={};", ptKey, ptPin);
        timedCache.remove(mobile);
        return new JdInfo().builder().cookie(cookie).build();
    }


    private Map<String, String> transSetCookie2Map(List<String> setCookiesList) {
        if (CollectionUtils.isEmpty(setCookiesList)) {
            return new HashMap<>();
        }
        return setCookiesList.stream()
                .map(item -> item.split(";"))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .filter(s -> {
                    final String[] split = s.split("=");
                    return split.length > 1 && StrUtil.isNotBlank(split[1]);
                })
                .distinct()
                .collect(Collectors.toMap(
                        s -> s.split("=")[0],
                        s -> s.split("=")[1],
                        (s1, s2) -> s2
                ));
    }

    private void checkErr(JSONObject responseJo) {
        Integer errCode = responseJo.getInteger("err_code");
        if (errCode != 0) {
            throw new BizException(responseJo.getString("err_msg"));
        }
    }
}
