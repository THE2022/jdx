package cn.yiidii.jdx.controller;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.QLConfig;
import cn.yiidii.jdx.model.R;
import cn.yiidii.jdx.model.dto.JdInfo;
import cn.yiidii.jdx.model.enums.SocialPlatformEnum;
import cn.yiidii.jdx.model.ex.BizException;
import cn.yiidii.jdx.service.JdService;
import cn.yiidii.jdx.service.QLService;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * JdController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final JdService jdService;
    private final QLService qlService;
    private final SystemConfigProperties systemConfigProperties;

    @GetMapping("/jd/smsCode")
    public R<JdInfo> qrCode(@RequestParam @NotNull(message = "请填写手机号") String mobile) throws Exception {
        Assert.isTrue(PhoneUtil.isMobile(mobile), () -> {
            throw new BizException("手机号格式不正确");
        });
        JdInfo jdInfo = jdService.sendSmsCode(mobile);
        log.info(StrUtil.format("{}发送了验证码", DesensitizedUtil.mobilePhone(mobile)));
        return R.ok(jdInfo, "发送验证码成功");
    }

    @PostMapping("/jd/login")
    public R<JdInfo> login(@RequestBody JSONObject paramJo) throws Exception {
        String mobile = paramJo.getString("mobile");
        String code = paramJo.getString("code");
        Assert.isTrue(StrUtil.isNotBlank(mobile), () -> {
            throw new BizException("手机号不能为空");
        });
        Assert.isTrue(PhoneUtil.isMobile(mobile), () -> {
            throw new BizException("手机号格式不正确");
        });
        Assert.isTrue(StrUtil.isNotBlank(code), () -> {
            throw new BizException("验证码不能为空");
        });

        // displayName不为空说明是ql模式
        String displayName = paramJo.getString("displayName");
        if (StrUtil.isNotBlank(displayName)) {
            String remark = paramJo.getString("remark");
            remark = StrUtil.isBlank(remark) ? mobile : remark;
            qlService.submitCk(mobile, code, displayName, remark);
            return R.ok(null, StrUtil.format("提交至【{}】成功", displayName));
        } else {
            JdInfo jdInfo = jdService.login(mobile, code);
            log.info(StrUtil.format("{}获取了京东Cookie", DesensitizedUtil.mobilePhone(mobile)));
            return R.ok(jdInfo, "获取cookie成功");
        }
    }

    @GetMapping("info")
    public R<?> getBaseInfo() {
        JSONObject jo = new JSONObject();
        jo.put("title", systemConfigProperties.getTitle());
        jo.put("notice", systemConfigProperties.getNotice());
        List<JSONObject> sources = systemConfigProperties.getSocialPlatforms().stream()
                .filter(e -> Objects.nonNull(SocialPlatformEnum.get(e.getSource())))
                .map(e -> {
                    SocialPlatformEnum socialPlatformEnum = SocialPlatformEnum.get(e.getSource());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("displayName", socialPlatformEnum.getDisplayName());
                    jsonObject.put("source", socialPlatformEnum.name());
                    jsonObject.put("url", socialPlatformEnum.getUrl());
                    jsonObject.put("iconBase64", socialPlatformEnum.getIconBase64());
                    return jsonObject;
                }).distinct().collect(Collectors.toList());
        jo.put("sources", sources);
        jo.put("qls", systemConfigProperties.getQls().stream().map(QLConfig::getDisplayName).distinct().collect(Collectors.toList()));
        return R.ok(jo);
    }
}
