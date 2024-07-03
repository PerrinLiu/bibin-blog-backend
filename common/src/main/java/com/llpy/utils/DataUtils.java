package com.llpy.utils;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Long> longToList(Long longValue) {
        if(longValue==null){
            return new ArrayList<>();
        }
        List<Long> list = new ArrayList<>();
        list.add(longValue);
        return list;
    }
}
