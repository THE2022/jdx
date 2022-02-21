package cn.yiidii.jdx.config.prop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yiidii.jdx.model.enums.SocialPlatformEnum;
import cn.yiidii.jdx.util.ScheduleTaskUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 *
 * @author ed w
 * @since 1.0
 */
@Data
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemConfigProperties implements InitializingBean {

    public static final String SYSTEM_CONFIG_FILE_PAH = System.getProperty("user.dir") + File.separator + "config" + File.separator + "config.json";
    private static boolean INIT = false;

    @JSONField(serialize = false, deserialize = false)
    private final ScheduleTaskUtil scheduleTaskUtil;

    private String title;
    private String notice;
    private List<SocialPlatform> socialPlatforms;
    private List<QLConfig> qls;


    @PostConstruct
    public void init() {
        INIT = true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 根据config.json赋值
        update(true);
    }

    public String update(boolean throwException) {
        try {
            String configStr = FileUtil.readUtf8String(SYSTEM_CONFIG_FILE_PAH);
            JSONObject configJo = JSONObject.parseObject(configStr);
            BeanUtil.copyProperties(configJo, this);
            return configJo.toJSONString();
        } catch (Exception e) {
            if (throwException) {
                throw new IllegalArgumentException(StrUtil.format("{}不存在", SYSTEM_CONFIG_FILE_PAH));
            }
            log.error("更新配置文件[{}]发生异常, e: {}", SYSTEM_CONFIG_FILE_PAH, e.getMessage());
            return null;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialPlatform {

        @NotNull(message = "平台不能为空")
        @Length(min = 1, message = "平台格式不正确")
        private String source;
        @NotNull(message = "客户端ID不能为空")
        @Length(min = 1, message = "客户端ID格式不正确")
        private String clientId;
        @NotNull(message = "客户端密钥不能为空")
        @Length(min = 1, message = "客户端密钥格式不正确")
        private String clientSecret;
        @NotNull(message = "重定向地址不能为空")
        @Length(min = 1, message = "重定向地址格式不正确")
        private String redirectUri;
        @NotNull(message = "管理员不能为空")
        @Length(min = 1, message = "管理员格式不正确")
        private String admin;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QLConfig {

        @NotNull(message = "displayName不能为空")
        private String displayName;
        @NotNull(message = "url不能为空")
        private String url;
        @NotNull(message = "clientId不能为空")
        private String clientId;
        @NotNull(message = "clientSecret不能为空")
        private String clientSecret;
    }

    public SocialPlatform getSocialConfig(String source) {
        return this.socialPlatforms.stream()
                .filter(s -> Objects.nonNull(SocialPlatformEnum.get(s.getSource())) && StrUtil.equals(source, s.getSource()))
                .findFirst()
                .orElse(null);
    }

    public SocialPlatform getSocialConfig(SocialPlatformEnum socialPlatformEnum) {
        return this.getSocialConfig(socialPlatformEnum.name());
    }

    public QLConfig getQLConfigByDisplayName(String displayName) {
        return qls.stream().filter(ql -> StrUtil.equalsIgnoreCase(displayName, ql.getDisplayName()))
                .findFirst().orElse(null);
    }

    public void startTimerTask() {
        scheduleTaskUtil.startCron("QL_timerRefreshToken", () -> {
            this.timerPersistSystemConfig();
        }, "0/30 * * * * ?");
    }

    private void timerPersistSystemConfig() {
        if (!INIT) {
            return;
        }
        Thread.currentThread().setName(String.format(Thread.currentThread().getName(), "SYS_timerPersistSystemConfig"));
        String prettyJa = JSONUtil.toJsonPrettyStr(JSONObject.toJSONString(this));
        FileUtil.writeString(prettyJa, SYSTEM_CONFIG_FILE_PAH, StandardCharsets.UTF_8);
    }
}
