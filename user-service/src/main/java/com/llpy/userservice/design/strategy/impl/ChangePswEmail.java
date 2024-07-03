package com.llpy.userservice.design.strategy.impl;

import com.llpy.enums.RedisKeyEnum;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.userservice.design.strategy.EmailStrategy;
import com.llpy.userservice.entity.User;
import com.llpy.userservice.entity.dto.MailDto;
import com.llpy.userservice.redis.RedisService;
import com.llpy.userservice.utils.EmailUtil;
import com.llpy.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 更改psw电子邮件
 *
 * @author llpy
 * @date 2024/06/26
 */
@Component
public class ChangePswEmail implements EmailStrategy {

    private final EmailUtil emailUtil;

    private final RedisService redisService;

    public ChangePswEmail(EmailUtil emailUtil, RedisService redisService) {
        this.emailUtil = emailUtil;
        this.redisService = redisService;
    }

    @Override
    public Result<?> sendEmail(String email, User user) {
        // 发送更改psw邮件
        //返回邮箱已存在
        if (user == null) {
            return Result.error(ResponseError.USER_NOT_EXIST);
        }

        //创建邮箱对象
        MailDto mailDto = new MailDto();
        String code = EmailUtil.generateRandomCode();
        String message = "<p style=\"color: #555; line-height: 1.6;\">" +
                "正在进行bibin账号的" +
                "<span style='font-size: 18px; font-weight: bold;'>密码重置</span>，您的验证码是：" +
                "<span style='font-size: 20px; font-weight: bold;'>" +
                code+
                "</span>" +
                "</p>" +
                "<p style=\"color: #555;\">此验证码将在5分钟内有效。</p>";
        mailDto.setTo(email);
        //调用工具类发送验证码
        MailDto mail = emailUtil.sendMail(mailDto, message);

        //如果状态码不为ok，返回发送失败
        String retCode = "ok";
        if (!mail.getStatus().equals(retCode)) {
            return Result.error(ResponseError.USER_EMAIL_ERROR);
        }
        String redisEmailKey = getString(code);

        return new Result<>("已发送，5分钟内有效~", 200, redisEmailKey);
    }

    private String getString(String code) {
        //生成一个不带‘ - ‘的uuid，用来和邮箱验证码一起存进redis
        String redisEmailKey = UUID.randomUUID().toString().replaceAll("-", "");
        //1.获得枚举的key加上uuid，2.获得验证码，3.获得设置的默认过期时间
        redisService.savaRedis(RedisKeyEnum.REDIS_KEY_EMAIL_CODE,code,redisEmailKey);
        return redisEmailKey;
    }
}
