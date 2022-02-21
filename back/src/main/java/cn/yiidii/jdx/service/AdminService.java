package cn.yiidii.jdx.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.QLConfig;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.SocialPlatform;
import cn.yiidii.jdx.model.enums.SocialPlatformEnum;
import cn.yiidii.jdx.model.ex.BizException;
import cn.yiidii.jdx.util.ScheduleTaskUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final SystemConfigProperties systemConfigProperties;
    private final ScheduleTaskUtil scheduleTaskUtil;

    public JSONArray getQLConfig() {
        List<QLConfig> qls = systemConfigProperties.getQls();
        return JSON.parseArray(JSONArray.toJSONString(qls));
    }

    public List<QLConfig> addQLConfig(QLConfig qlConfig) {
        QLConfig exist = systemConfigProperties.getQLConfigByDisplayName(qlConfig.getDisplayName());
        if (Objects.nonNull(exist)) {
            throw new BizException(StrUtil.format("已存在该名称[{}]配置", qlConfig.getDisplayName()));
        }
        List<QLConfig> qls = systemConfigProperties.getQls();
        qls.add(qlConfig);
        return qls;
    }

    public List<QLConfig> delQLConfig(String displayName) {
        QLConfig exist = systemConfigProperties.getQLConfigByDisplayName(displayName);
        if (Objects.isNull(exist)) {
            throw new BizException("青龙配置不存在");
        }
        List<QLConfig> qls = systemConfigProperties.getQls();
        qls = qls.stream().filter(ql -> !StrUtil.equalsIgnoreCase(displayName, ql.getDisplayName())).collect(Collectors.toList());
        systemConfigProperties.setQls(qls);
        return qls;
    }

    public JSONObject updateWebsiteConfig(JSONObject paramJo) {
        String title = paramJo.getString("title");
        String notice = paramJo.getString("notice");

        JSONObject result = new JSONObject();
        result.put("title", systemConfigProperties.getTitle());
        result.put("notice", systemConfigProperties.getNotice());
        if (StrUtil.isNotBlank(title)) {
            systemConfigProperties.setTitle(title);
            result.put("title", title);
        }
        if (StrUtil.isNotBlank(notice)) {
            systemConfigProperties.setNotice(notice);
            result.put("notice", notice);
        }
        return result;
    }

    public List<SocialPlatform> saveSocialConfig(SocialPlatform socialPlatformParam) {
        String source = socialPlatformParam.getSource();
        SocialPlatformEnum socialPlatformEnum = SocialPlatformEnum.get(source);
        if (Objects.isNull(socialPlatformEnum)) {
            throw new BizException(StrUtil.format("暂未支持该平台[{}]", source));
        }
        SocialPlatform exist = systemConfigProperties.getSocialConfig(socialPlatformParam.getSource());
        List<SocialPlatform> socialPlatforms = systemConfigProperties.getSocialPlatforms();
        if (Objects.nonNull(exist)) {
            BeanUtil.copyProperties(socialPlatformParam, exist);
        } else {
            socialPlatforms.add(socialPlatformParam);
        }
        return socialPlatforms;
    }

    public List<SocialPlatform> delSocialConfig(String source) {
        List<SocialPlatform> socialPlatforms = systemConfigProperties.getSocialPlatforms();
        if (socialPlatforms.size() == 1) {
            throw new BizException("最后一个配置不能删除");
        }
        SocialPlatform exist = systemConfigProperties.getSocialConfig(source);
        if (Objects.isNull(exist)) {
            throw new BizException("配置不存在, 请先新增");
        }
        socialPlatforms = socialPlatforms.stream()
                .filter(s -> StrUtil.equals(source, s.getSource()))
                .collect(Collectors.toList());
        systemConfigProperties.setSocialPlatforms(socialPlatforms);
        return socialPlatforms;
    }

}
