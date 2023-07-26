package com.llpy.utils;



import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 非法字符 数据校验 unlawful SYMBOL
 *
 * @author TT
 * @return validationUtils
 * @date 2021-04-22
 */
public class ValidationUtils {


    private final static Pattern SYMBOL = compile("^[\\u4E00-\\u9FA5A-Za-z0-9_.,]{0,384}$");
    private final static Pattern PHONE = compile("^(13[0-9]|14[0-9]|15[0-9]|166|17[0-9]|18[0-9]|19[8|9])\\d{8}$");
    private final static Pattern NAME = compile("^[\\u4E00-\\u9FA5A-Za-z0-9]+$");
    private final static Pattern NONNEGATIVEINTEGER = compile("^\\d+$");
    private final static Pattern ID_CARD = compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
    private final static Pattern PWD = compile("^[a-zA-Z]\\w{5,17}$");
    private final static Pattern STRONG_PWD = compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$");
    private final static Pattern BLANK = compile("\\s(?=\\s)");
    private final static Pattern QQ = compile("^[1-9][0-9]{4,12}$");
    private final static Pattern EMAIL = compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    private final static Pattern PROVE = compile("^(?=.{1,10}$)\\d+(-\\d+)$");
    private final static Pattern NUMBER = compile("^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,8})?$");
    private final static Pattern SQL_UNLAWFUL_SYMBOL = compile("([^\"]{0})(SELECT)|(DELETE)|(UPDATE)|(INSERT)|(DROP)|(TRUNCATE)|(ALTER)|(TABLE)|(OR)|(AND)|(ORDER)|(BY)|(select)|(delete)|(drop)|(truncate)|(alter)|(table)|(or)|(and)|(order)|(by)([^\"]{0})");

    /**
     * unlawful symbol非法字符校验
     *
     * @author TT
     */
    public static Boolean validationSymbol(String first, String second) {

        if (StrUtil.isNotEmpty(first) && StrUtil.isNotEmpty(second)) {
            Matcher a = SYMBOL.matcher(first);
            Matcher b = SYMBOL.matcher(second);
            if (!a.matches()) {
                return false;
            }
            if (!b.matches()) {
                return false;
            }

            return a.matches();
        }
        return null;
    }

    public static Boolean validationSymbol(String first) {


        if (StrUtil.isNotEmpty(first)) {
            Matcher a = SYMBOL.matcher(first);
            if (!a.matches()) {
                return false;
            }

            return a.matches();
        }
        return null;
    }

    public static Boolean validationSymbol(Integer first) {

        if (first != null) {
            Matcher a = SYMBOL.matcher(String.valueOf(first));
            if (!a.matches()) {
                return false;
            }
            return a.matches();
        }
        return null;
    }

    public static Boolean validationSymbol(Integer first, Integer second) {
        if (first != null && second != null) {
            Matcher a = SYMBOL.matcher(String.valueOf(first));
            Matcher b = SYMBOL.matcher(String.valueOf(second));

            if (!a.matches()) {
                return false;
            }
            if (!b.matches()) {
                return false;
            }
            return a.matches();
        }
        return null;
    }

    public static Boolean validationSymbol(String first, Integer second) {

        if (StrUtil.isNotEmpty(first) && second != null) {
            Matcher a = SYMBOL.matcher(first);
            Matcher b = SYMBOL.matcher(String.valueOf(second));
            if (!a.matches()) {
                return false;
            }
            if (!b.matches()) {
                return false;
            }

            return a.matches();
        }
        return null;
    }


    /**
     * sql关键字校验,函数校验
     *
     * @author TT
     */
//    public static Boolean validationSQL(String first) {
//
//        try {
//            if (StrUtil.isNotEmpty(first)) {
//                Matcher m = SQL_UNLAWFUL_SYMBOL.matcher(first);
//                if (!m.matches()) {
//                    return false;
//                } else {
//                    return m.matches();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 手机号码校验 11位有效数字
     *
     * @author TT
     */
    public static Boolean validationPhone(Long phone) {
        if (phone != null) {
            Matcher m = PHONE.matcher(String.valueOf(phone));
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }
    public static Boolean validationPhone(String phone) {
        if (phone != null) {
            Matcher m = PHONE.matcher(phone);
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * 用户名称校验(汉字和英文还有数字)
     *
     * @author TT
     */
    public static Boolean validationUser(String userName) {
        if (StrUtil.isNotEmpty(userName)) {
            Matcher m = NAME.matcher(userName);
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * 非负整数(0和正整数)校验
     *
     * @author TT
     */
    public static Boolean validationNonnegative(Long value) {
        if (value != null) {
            Matcher m = NONNEGATIVEINTEGER.matcher(String.valueOf(value));
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    public static Boolean validationNonnegative(Integer value) {
        if (value != null) {
            Matcher m = NONNEGATIVEINTEGER.matcher(String.valueOf(value));
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * 身份证精准校验
     *
     * @author TT
     */
    public static Boolean validationIdCard(String number) {
        if (number != null) {
            Matcher m = ID_CARD.matcher(number);
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * 密码格式(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)精准校验
     *
     * @author TT
     */
    public static Boolean validationPwd(String password) {
        if (password != null) {
            Matcher m = PWD.matcher(password);
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * 强密码校验(必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间)
     *
     * @author TT
     */
    public static Boolean validationStrongPwd(String password) {
        if (password != null) {
            Matcher m = STRONG_PWD.matcher(password);
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * 空白格校验
     *
     * @author TT
     */
//    public static Boolean validationBlank(String value) {
//            Matcher m = BLANK.matcher(value);
//            if (!m.matches()) {
//                return false;
//            } else {
//                return m.matches();
//            }
//    }

    public static Boolean validationBlank(Integer value) {
        if (value != null) {
            Matcher m = BLANK.matcher(String.valueOf(value));
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * 邮箱格式简单校验
     *
     * @author TT
     */
    public static Boolean validationEmail(String value) {
        if (value != null) {
            Matcher m = EMAIL.matcher(value);
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * qq格式简单校验
     *
     * @author TT
     */
    public static Boolean validationQQ(Long value) {
        if (value != null) {
            Matcher m = QQ.matcher(String.valueOf(value));
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;

    }

    /**
     * 数字-数字(适用于查凭证中筛选条件凭证字号)
     *
     * @author TT
     * @date 2021-08-20
     */
    public static Boolean validationProveNo(String proveNo) {
        if (proveNo != null) {
            Matcher m = PROVE.matcher((proveNo));
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;
    }
    /**
     * 钱的输入格式
     *
     * @author TT
     * @date 2021-08-20
     */
    public static Boolean validationNumber(String value) {
        if (value != null) {
            Matcher m = NUMBER.matcher((value));
            if (!m.matches()) {
                return false;
            } else {
                return m.matches();
            }
        }
        return null;
    }
}
