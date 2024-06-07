package com.llpy.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 网关应用程序
 *
 * @author LLPY
 * @date 2023/11/08
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.llpy"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
