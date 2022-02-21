package cn.yiidii.jdx.support;

import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import cn.yiidii.jdx.service.QLService;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 配置文件监听
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemConfigFileListener implements InitializingBean {

    private final SystemConfigProperties systemConfigProperties;
    private final QLService qlService;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 启动文件监听
        SimpleWatcher simpleWatcher = new SimpleWatcher() {
            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                String update = systemConfigProperties.update(false);
                try {
                    if (StrUtil.isNotBlank(update)) {
                        log.info("配置文件变更: {}", update);
                        // 重新启动定时任务等
                        qlService.startTimerTask();
                        systemConfigProperties.startTimerTask();
                    }
                } catch (Exception e) {
                    // ignore
                }
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                log.info("配置文件删除, 系统停止");
                AbstractApplicationContext ctx = (AbstractApplicationContext) SpringUtil.getApplicationContext();
                ctx.registerShutdownHook();
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                log.info("配置文件删除, 系统停止");
                AbstractApplicationContext ctx = (AbstractApplicationContext) SpringUtil.getApplicationContext();
                ctx.registerShutdownHook();
            }
        };
        // 延迟处理监听事件
        WatchMonitor.createAll(SystemConfigProperties.SYSTEM_CONFIG_FILE_PAH, new DelayWatcher(simpleWatcher, 1000)).start();
    }

}
