package cn.yiidii.jdx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JdxApplication
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "cn.yiidii")
public class JdxApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdxApplication.class, args);
        log.info("系统启动成功");
    }

}
