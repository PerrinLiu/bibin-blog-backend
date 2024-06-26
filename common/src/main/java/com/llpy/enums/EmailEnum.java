package com.llpy.enums;

/**
 * 电子邮件枚举
 *
 * @author llpy
 * @date 2024/06/26
 */
public enum EmailEnum {


    REGISTER(1, "registerEmail", "注册邮件"),
    CHANGE_PSW(2,"changePswEmail","更改密码邮件");
    private final int key;
    private final String name;


    EmailEnum(int key, String name, String keyInfo) {
        this.key = key;
        this.name = name;
    }


    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
