package cn.yiidii.jdx.controller;

import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.QLConfig;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.SocialPlatform;
import cn.yiidii.jdx.model.R;
import cn.yiidii.jdx.service.AdminService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final SystemConfigProperties systemConfigProperties;

    @GetMapping("ql")
    public R<?> qlConfig() {
        JSONArray qlConfig = adminService.getQLConfig();
        return R.ok(qlConfig);
    }

    @PostMapping("ql")
    public R<?> addQLConfig(@RequestBody @Validated QLConfig qlConfig) {
        List<QLConfig> qlConfigs = adminService.addQLConfig(qlConfig);
        return R.ok(qlConfigs, "添加成功");
    }

    @DeleteMapping("ql")
    public R<?> delQLConfig(@RequestParam @NotNull(message = "displayName不能为空") String displayName) {
        List<QLConfig> qlConfigs = adminService.delQLConfig(displayName);
        return R.ok(qlConfigs, "删除成功");
    }

    @GetMapping("config")
    public R<?> getConfig() {
        JSONObject result = new JSONObject();
        result.put("title", systemConfigProperties.getTitle());
        result.put("notice", systemConfigProperties.getNotice());
        result.put("socialPlatforms", systemConfigProperties.getSocialPlatforms());
        return R.ok(result);
    }

    @PutMapping("websiteConfig")
    public R<?> updateWebsiteConfig(@RequestBody JSONObject paramJo) {
        JSONObject websiteConfig = adminService.updateWebsiteConfig(paramJo);
        return R.ok(websiteConfig, "修改成功");
    }

    @PostMapping("socialConfig")
    public R<?> saveSocialConfig(@RequestBody @Validated SocialPlatform socialPlatform) {
        List<SocialPlatform> socialPlatforms = adminService.saveSocialConfig(socialPlatform);
        return R.ok(socialPlatforms, "保存成功");
    }

    @DeleteMapping("socialConfig")
    public R<?> delSocialConfig(@RequestParam @NotNull(message = "source不能为空") String source) {
        List<SocialPlatform> socialPlatforms = adminService.delSocialConfig(source);
        return R.ok(socialPlatforms, "删除成功");
    }
}
