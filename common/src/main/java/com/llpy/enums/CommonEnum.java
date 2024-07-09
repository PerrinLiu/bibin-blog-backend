package com.llpy.enums;

import lombok.Getter;

/**
 * 公共枚举
 *
 * @author llpy
 * @date 2024/07/09
 */
@Getter
public enum CommonEnum {
    SYSTEM_ADMIN("系统管理员",1024*1024*1024*1024L),
    USER("普通用户",30*1024*1024L),
    ADMIN("管理员",400*1024*1024L),
    ;
    private final String key;

    private final Long maxSize;


    CommonEnum(String key,Long maxSize) {
        this.key = key;
        this.maxSize = maxSize;
    }
}
