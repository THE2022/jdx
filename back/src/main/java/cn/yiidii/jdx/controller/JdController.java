package cn.yiidii.jdx.controller;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.jdx.model.R;
import cn.yiidii.jdx.model.dto.JdInfo;
import cn.yiidii.jdx.model.ex.BizException;
import cn.yiidii.jdx.service.JdService;
import com.alibaba.fastjson.JSONObject;
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
@RequestMapping("jd")
@RequiredArgsConstructor
public class JdController {

    private final JdService jdService;

    @GetMapping("smsCode")
    public R<JdInfo> qrCode(@RequestParam @NotNull(message = "请填写手机号") String mobile) throws Exception {
        Assert.isTrue(PhoneUtil.isMobile(mobile), () -> {
            throw new BizException("手机号格式不正确");
        });
        JdInfo jdInfo = jdService.sendSmsCode(mobile);
        log.info(StrUtil.format("{}发送了验证码", DesensitizedUtil.mobilePhone(mobile)));
        return R.ok(jdInfo, "发送验证码成功");
    }

    @PostMapping("login")
    public R<JdInfo> check(@RequestBody JSONObject paramJo) throws Exception {
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
        JdInfo jdInfo = jdService.login(mobile, code);
        log.info(StrUtil.format("{}获取了京东Cookie", DesensitizedUtil.mobilePhone(mobile)));
        return R.ok(jdInfo, "获取cookie成功");
    }
}
