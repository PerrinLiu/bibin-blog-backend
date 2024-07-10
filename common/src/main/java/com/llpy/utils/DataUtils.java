package com.llpy.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    /**
     * 按天获取arr,格式化为yyyy-MM-dd
     *
     * @param day 白天
     * @return {@code int[]}
     */
    public static String[] getArrByDay(int day){
        // 定义日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 获取今天的日期
        LocalDate today = LocalDate.now();
        // 获取184天前的日期
        LocalDate startDate = today.minusDays(day); // 包含今天，总共184天

        // 存储日期的列表
        List<String> dateList = new ArrayList<>();

        // 遍历这184天的每一天
        LocalDate date = startDate;
        while (!date.isAfter(today)) {
            // 格式化日期
            String formattedDate = date.format(formatter);
            // 添加到列表中
            dateList.add(formattedDate);
            // 日期加1天
            date = date.plusDays(1);
        }

        // 将列表转换为数组

        // 将列表转换为数组
        return dateList.toArray(new String[0]);
    }

    /**
     * 获得今天
     *
     * @return {@code String}
     */
    public static String getToday() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }


    /**
     * 提取文章中的img的链接信息
     *
     * @param html html
     * @return {@code List<String>}
     */
    public static List<String> extractImgSrc(String html) {
        List<String> imgSrcList= new ArrayList<>();

        Pattern pattern = Pattern.compile("<img\\s+[^>]*src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");

        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            imgSrcList.add(matcher.group(1)); // 获取整个img标签
        }

        return imgSrcList;
    }
}
