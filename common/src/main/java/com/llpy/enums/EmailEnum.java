package com.llpy.enums;

import lombok.Getter;

/**
 * 电子邮件枚举
 *
 * @author llpy
 * @date 2024/06/26
 */
@Getter
public enum EmailEnum {


    REGISTER("register", "registerEmail", "注册邮件"),
    CHANGE_PSW("changePsw","changePswEmail","更改密码邮件"),
    DIARY_PASS( "diaryPass", "diaryPassEmail","日记审核通过邮件"),
    DIARY_REJECT( "diaryReject", "diaryRejectEmail","日记审核失败邮件"),
    ;
    private final String key;
    private final String name;
    private final String keyInfo;


    EmailEnum(String key, String name, String keyInfo) {
        this.key = key;
        this.name = name;
        this.keyInfo = keyInfo;
    }


}
