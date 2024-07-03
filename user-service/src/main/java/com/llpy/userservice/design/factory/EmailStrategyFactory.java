package com.llpy.userservice.design.factory;

import com.llpy.userservice.enums.EmailEnum;
import com.llpy.userservice.design.strategy.EmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 电子邮件工厂
 *
 * @author llpy
 * @date 2024/06/26
 */
@Component
public class EmailStrategyFactory  {

    private static final Map<String, EmailStrategy> STRATEGY_MAP = new HashMap<>();

    @Autowired
    public EmailStrategyFactory(ApplicationContext context) {
        for (EmailEnum value : EmailEnum.values()) {
            STRATEGY_MAP.put(value.getKey(), (EmailStrategy) context.getBean(value.getName()));
        }
    }

    public EmailStrategy getEmailStrategy(String emailType) {
        return STRATEGY_MAP.get(emailType);
    }



}
