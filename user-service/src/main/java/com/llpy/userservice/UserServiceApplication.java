package com.llpy.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 用户服务应用程序
 *
 * @author LLPY
 * @date 2023/11/08
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.llpy"})
@EnableSwagger2
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
