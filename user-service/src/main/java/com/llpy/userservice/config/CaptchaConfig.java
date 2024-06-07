package com.llpy.userservice.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * captcha配置
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties properties = new Properties();
        // 配置验证码属性
        properties.setProperty("kaptcha.image.width", "120");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.textproducer.char.string", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 更多配置...
        captchaProducer.setConfig(new Config(properties));
        return captchaProducer;
    }
}
