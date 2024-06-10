//package com.llpy.userservice.config;
//
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//@Component
//public class NacosConfigPrinter implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final Environment environment;
//
//    public NacosConfigPrinter(Environment environment) {
//        this.environment = environment;
//    }
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        // 打印Nacos配置
//        System.out.println("Nacos配置:");
//        System.out.println(environment.getProperty("spring.redis.host"));
//        System.out.println(environment.getProperty("aliyun.oss.endpoint"));
//        System.out.println(environment.getProperty("aliyun.oss.accessKeyId"));
//        System.out.println(environment.getProperty("aliyun.oss.accessKeySecret"));
//        System.out.println(environment.getProperty("aliyun.oss.bucketName"));
//    }
//}
