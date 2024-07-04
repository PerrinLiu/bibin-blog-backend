package com.llpy.userservice.design.strategy;

import com.llpy.model.Result;
import com.llpy.userservice.entity.User;

/**
 * 电子邮件策略
 *
 * @author llpy
 * @date 2024/06/26
 */
public interface EmailStrategy {
    public Result<?> sendEmail(String email,  User user,String message);
}
