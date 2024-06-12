package com.llpy.config;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将所有带有id的返回值都以字符串返回前端
 */
@Configuration
public class CustomFastJsonConfig {
    @Bean
    FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        //1.需要定义一个convert转换消息的对象
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        //2.添加fastJson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //3.设置id字段为字符串
        fastJsonConfig.setSerializeFilters((ValueFilter) (object, name, value) -> {
            if (name.contains("Id") || name.contains("id")) {
                return value + "";
            }
            return value;
        });

        //4.在convert中添加配置信息.
        converter.setFastJsonConfig(fastJsonConfig);
        return converter;
    }
}

