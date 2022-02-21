package cn.yiidii.jdx.service;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.QLConfig;
import cn.yiidii.jdx.model.dto.JdInfo;
import cn.yiidii.jdx.model.ex.BizException;
import cn.yiidii.jdx.util.ScheduleTaskUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * QLService
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QLService {

    private final static Map<String, String> QL_TOKEN_CACHE = new ConcurrentHashMap<>(16);

    public final JdService jdService;
    public final SystemConfigProperties systemConfigProperties;
    private final ScheduleTaskUtil scheduleTaskUtil;

    public void submitCk(String mobile, String code, String displayName, String remark) throws Exception {
        JdInfo jdInfo = jdService.login(mobile, code);
        String cookie = jdInfo.getCookie();
        this.addEnv(displayName, "JD_COOKIE", cookie, remark);
    }

    public void addEnv(String displayName, String name, String value, String remark) {
        QLConfig qlConfig = systemConfigProperties.getQLConfigByDisplayName(displayName);
        if (Objects.isNull(qlConfig)) {
            throw new BizException(StrUtil.format("青龙节点【{}】不存在", displayName));
        }
        // 获取存在的env
        String ptPin = getPtPinFromCK(value);
        JSONObject existEnv = this.getExistCK(displayName, StrUtil.format("pt_pin={};", ptPin));

        // 推送青龙
        if (existEnv.isEmpty()) {
            // 新增逻辑
            JSONObject envJo = new JSONObject();
            envJo.put("name", name);
            envJo.put("value", value);
            envJo.put("remarks", remark);
            JSONArray paramJa = new JSONArray();
            paramJa.add(envJo);
            try {
                HttpResponse response = HttpRequest.post(qlConfig.getUrl().concat("open/envs"))
                        .bearerAuth(this.getQLToken(displayName))
                        .body(paramJa.toJSONString())
                        .execute();
                log.info(StrUtil.format("[青龙 - {}] 添加环境变量, 参数: {}, 响应: {}", displayName, envJo.toJSONString(), response.body()));
            } catch (Exception e) {
                throw new BizException("连接青龙发生异常, 请联系系统管理员");
            }
        } else {
            // 更新并且启用
            // 更新
            JSONObject envJo = new JSONObject();
            envJo.put("name", name);
            envJo.put("value", value);
            envJo.put("remarks", remark);
            envJo.put("_id", existEnv.getString("_id"));
            try {
                HttpResponse response = HttpRequest.put(qlConfig.getUrl().concat("open/envs"))
                        .bearerAuth(this.getQLToken(displayName))
                        .body(envJo.toJSONString())
                        .execute();
                log.info(StrUtil.format("[青龙 - {}] 更新环境变量, 参数: {}, 响应: {}", displayName, envJo.toJSONString(), response.body()));

                // 启用
                if (existEnv.getInteger("status") == 1) {
                    JSONArray paramJa = new JSONArray();
                    paramJa.add(existEnv.getString("_id"));
                    response = HttpRequest.put(qlConfig.getUrl().concat("open/envs/enable"))
                            .bearerAuth(this.getQLToken(displayName))
                            .body(paramJa.toJSONString())
                            .execute();
                    log.info(StrUtil.format("[青龙 - {}] 启用环境变量, 参数: {}, 响应: {}", displayName, paramJa.toJSONString(), response.body()));
                }
            } catch (Exception e) {
                throw new BizException("连接青龙发生异常, 请联系系统管理员");
            }
        }

    }

    public List<JSONObject> searchEnv(String displayName, String searchValue) {
        QLConfig qlConfig = systemConfigProperties.getQLConfigByDisplayName(displayName);
        if (Objects.isNull(qlConfig)) {
            throw new BizException(StrUtil.format("青龙节点【{}】不存在", displayName));
        }
        try {
            HttpResponse response = HttpRequest.get(StrUtil.format("{}open/envs?searchValue={}", qlConfig.getUrl(), searchValue))
                    .bearerAuth(this.getQLToken(displayName))
                    .execute();
            log.info(StrUtil.format("[青龙 - {}] 搜索环境变量, 参数: {}, 响应: {}", displayName, searchValue, response.body()));
            JSONObject respJo = JSONObject.parseObject(response.body());
            JSONArray data = respJo.getJSONArray("data");

            // 返回
            return data.stream().map(d -> {
                JSONObject tmp = (JSONObject) d;
                JSONObject jo = new JSONObject();
                jo.put("_id", tmp.getString("_id"));
                jo.put("name", tmp.getString("name"));
                jo.put("value", tmp.getString("value"));
                jo.put("status", tmp.getInteger("status"));
                jo.put("remarks", tmp.getString("remarks"));
                return jo;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BizException("连接青龙发生异常, 请联系系统管理员");
        }
    }

    public String getQLToken(String displayName) {
        QLConfig qlConfig = systemConfigProperties.getQLConfigByDisplayName(displayName);
        if (Objects.isNull(qlConfig)) {
            throw new BizException(StrUtil.format("青龙节点【{}】不存在", displayName));
        }
        String token = QL_TOKEN_CACHE.get(displayName);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return this.refreshToken(qlConfig);
    }

    public String refreshToken(QLConfig ql) {
        HttpResponse response = HttpRequest.get(StrUtil.format("{}open/auth/token?client_id={}&client_secret={}", ql.getUrl(), ql.getClientId(), ql.getClientSecret())).execute();
        if (response.getStatus() == HttpStatus.HTTP_OK) {
            JSONObject respJo = JSONObject.parseObject(response.body());
            Integer code = respJo.getInteger("code");
            if (code == HttpStatus.HTTP_OK) {
                String token = respJo.getJSONObject("data").getString("token");
                QL_TOKEN_CACHE.put(ql.getDisplayName(), token);
                return token;
            }
        }
        return null;
    }

    public void startTimerTask() {
        scheduleTaskUtil.startCron("QL_timerRefreshToken", () -> {
            this.timerRefreshToken();
        }, "0 0/1 * * * ?");
    }

    private void timerRefreshToken() {
        List<QLConfig> qlConfigs = systemConfigProperties.getQls();
        qlConfigs.forEach(ql -> refreshToken(ql));

    }

    private String getPtPinFromCK(String cookie) {
        cookie = StrUtil.isBlank(cookie) ? "" : ReUtil.replaceAll(cookie, "\\s+", "");
        return Arrays.stream(cookie.split(";"))
                .filter(e -> e.contains("pt_pin"))
                .findFirst().orElse("")
                .split("=")[1];
    }

    /**
     * 获取存在的CK
     *
     * @param displayName displayName
     * @param cookie      cookie
     * @return JSONObject
     */
    private JSONObject getExistCK(String displayName, String cookie) {
        List<JSONObject> envs = this.searchEnv(displayName, cookie);
        if (envs.size() == 0) {
            return new JSONObject();
        } else if (envs.size() == 1) {
            return envs.get(0);
        } else {
            // 通过【pt_pin=xxx;】搜索出来有多个的话, 返回正常状态(status=0)的第一个， 如果没有正常的, 就返回禁用状态(status=1)的第一个
            Map<Integer, List<JSONObject>> statusMap = envs.stream().collect(Collectors.groupingBy(e -> e.getInteger("status")));
            if (statusMap.get(0).size() >= 1) {
                return statusMap.get(0).get(0);
            } else if (statusMap.get(1).size() >= 1) {
                return statusMap.get(1).get(0);
            } else {
                return new JSONObject();
            }

        }
    }
}
