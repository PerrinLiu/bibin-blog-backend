package com.llpy.utils;

/**
 * 校验数据工具类
 *
 * @author llpy
 * @date 2024/06/30
 */
public class DataUtils {


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
