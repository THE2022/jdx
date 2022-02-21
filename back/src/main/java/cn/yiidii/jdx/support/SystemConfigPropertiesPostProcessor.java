package cn.yiidii.jdx.support;

import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Component
public class SystemConfigPropertiesPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof SystemConfigProperties) {
            // 检查
//            List<RobotConfig> robotConfigs = ((SystemConfigProperties) bean).getRobot();
//            Set<Long> configuredRobotQqs = robotConfigs.stream().map(RobotConfig::getQq).collect(Collectors.toSet());
//            Set<String> configuredRobotNames = robotConfigs.stream().map(RobotConfig::getName).collect(Collectors.toSet());
//            String errMessage =
//                    robotConfigs.size() != configuredRobotQqs.size() ? "配置的机器人QQ存在重复, 请检查! 系统退出..." :
//                            robotConfigs.size() != configuredRobotNames.size() ? "配置的机器人名称存在重复, 请检查! 系统退出..." : "";
//
//            if (StrUtil.isNotBlank(errMessage)) {
//                log.error(errMessage);
//                throw new RuntimeException(errMessage);
//            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
