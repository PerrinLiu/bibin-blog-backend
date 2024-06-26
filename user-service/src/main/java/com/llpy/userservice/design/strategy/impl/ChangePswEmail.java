package com.llpy.userservice.design.strategy.impl;

import com.llpy.model.Result;
import com.llpy.userservice.design.strategy.EmailStrategy;
import com.llpy.userservice.entity.User;
import org.springframework.stereotype.Component;

/**
 * 更改psw电子邮件
 *
 * @author llpy
 * @date 2024/06/26
 */
@Component
public class ChangePswEmail implements EmailStrategy {
    @Override
    public Result<?> sendEmail(String email, Integer type, User user) {

        // 发送更改psw邮件
        return null;
    }
}
