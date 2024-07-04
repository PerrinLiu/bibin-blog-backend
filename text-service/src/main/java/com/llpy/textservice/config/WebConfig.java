package com.llpy.textservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @author llpy
 * @date 2024/07/04
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = "/path/to/upload/dir";
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
