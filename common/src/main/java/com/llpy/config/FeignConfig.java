package com.llpy.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 外部配置
 *
 * @author llpy
 * @date 2024/07/01
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("gatewayKey", "xiaoKeAiXiangCaiDeMiYao");
                // 你可以添加更多的请求头
            }
        };
    }
}
