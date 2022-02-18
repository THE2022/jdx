package cn.yiidii.jdx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * JdxApplication
 *
 * @author ed w
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = "cn.yiidii")
//@ComponentScan(basePackages = {"cn.yiidii"})
public class JdxApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdxApplication.class, args);
    }

}
