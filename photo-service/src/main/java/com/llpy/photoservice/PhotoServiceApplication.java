package com.llpy.photoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"com.llpy"})
public class PhotoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoServiceApplication.class, args);
    }

}
