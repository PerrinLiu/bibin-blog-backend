package com.llpy.utils;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexUtils {
    /**
     * 判断是否是正确的邮箱地址
     *
     * @param email 邮箱
     * @return boolean true,通过，false，没通过
     */

    public boolean isEmail(String email) {

        if (Strings.isNullOrEmpty(email))

            return false;

        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

        return email.matches(regex);

    }

    /**
     * 判断是否含有中文，仅适合中国汉字，不包括标点
     *
     * @param text
     * @return boolean true,通过，false，没通过
     */

    public boolean isChinese(String text) {

        if (Strings.isNullOrEmpty(text))

            return false;

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

        Matcher m = p.matcher(text);

        return m.find();

    }

    /**
     * 判断是否正整数
     *
     * @return boolean true,通过，false，没通过
     * @parahttp://m number 整数
     * 数字
     */

    public boolean isNumber(String number) {

        if (Strings.isNullOrEmpty(number))

            return false;

        String regex = "[0-9]*";

        return number.matches(regex);

    }

    /**
     * 判断几位小数(正数)
     *
     * @param decimal 数字
     * @param count   小数位数
     * @return boolean true,通过，false，没通过
     */

    public boolean isDecimal(String decimal, int count) {

        if (Strings.isNullOrEmpty(decimal))

            return false;

        String regex = "^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){" + count

                + "})?$";

        return decimal.matches(regex);

    }

    /**
     * 判断是否是手机号码
     *
     * @param phoneNumber 移动手机号码
     * @return boolean true,通过，false，没通过
     */
    public boolean isPhoneNumber(String phoneNumber) {

        if (Strings.isNullOrEmpty(phoneNumber))

            return false;
        String regex = "^1\\d{10}$";
        return phoneNumber.matches(regex);
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param text 特殊字符
     * @return boolean true,通过，false，没通过
     */

    public boolean hasSpecialChar(String text) {

        if (Strings.isNullOrEmpty(text))

            return false;

        // 如果不包含特殊字符
        return text.replaceAll("[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0;

    }

    private boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;

    }

}