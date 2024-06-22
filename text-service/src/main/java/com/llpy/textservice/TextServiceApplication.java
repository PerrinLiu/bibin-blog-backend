package com.llpy.textservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 文本服务应用程序
 *
 * @author llpy
 * @date 2024/06/22
 */
@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.llpy"})
@EnableAsync
public class TextServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextServiceApplication.class, args);
    }

}
