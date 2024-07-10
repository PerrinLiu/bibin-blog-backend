package com.llpy.userservice.design.strategy.impl;

import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.userservice.design.strategy.EmailStrategy;
import com.llpy.userservice.entity.User;
import com.llpy.userservice.entity.dto.MailDto;
import com.llpy.userservice.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日记电子邮件
 *
 * @author llpy
 * @date 2024/07/04
 */
@Component
public class DiaryPassEmail implements EmailStrategy {

    @Autowired
    private EmailUtil emailUtil;

    @Override
    public Result<?> sendEmail(String email, User user,String message) {
            //创建邮箱对象
            MailDto mailDto = new MailDto();
            String msg =
                    "<p style=\"color: #555; line-height: 1.6;\">" +
                    "您发布的标题为<span style='font-size: 32px; font-weight: bold;'>"+ message +"</span>" +
                    "的碎片已经<span style='font-size: 24px; font-weight: bold;color: green;'>通过</span>审核，可上线查看~</p>";
            mailDto.setTo(email);
            //调用工具类发送验证码
            MailDto mail = emailUtil.sendMail(mailDto, msg);

            //如果状态码不为ok，返回发送失败
            String retCode = "ok";
            if (!mail.getStatus().equals(retCode)) {
                return Result.error(ResponseError.USER_EMAIL_ERROR);
            }

            return Result.success();
    }
}
