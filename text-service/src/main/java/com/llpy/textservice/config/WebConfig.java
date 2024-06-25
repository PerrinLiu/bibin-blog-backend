package com.llpy.textservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = "/path/to/upload/dir";
        registry.addResourceHandler("/files/common/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
