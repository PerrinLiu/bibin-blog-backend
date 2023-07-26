
package com.llpy.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 高频方法集合类
 */
public class ToolUtil {

    /**
     * 文件夹命名规范
     *  于是有如下匹配 首字符: [^\s\/??"<>|]尾字符: [^\s\/??"<>|.]
     *  其它字符: (\x20|[^\s\/??"<>|])* \s
     *  只能匹配下面六种字符（via: java.util.regex.Pattern）： 半角空格（ ） 水平制表符（\t） 竖直制表符 回车（\r） 换行（\n） 换页符（\f）
     */
    public static final String FOLDER_NAME = "^\\s\\\\/:\\*\\?\\\"<>\\|[^\\s\\\\/:\\\\?\\\"<>\\|\\.]$";
    /**
     * 获取随机位数的字符串
     *
     * @author fengshuonan
     * @Date 2017/8/24 14:09
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandomNum(int length) {
        //保存数字0-9 和 大小写字母
        String string = "0123456789";
        //声明一个StringBuffer对象sb 保存 验证码
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <length; i++) {
            //创建一个新的随机数生成器
            Random random = new Random();
            //返回[0,string.length)范围的int值    作用：保存下标
            int index = random.nextInt(string.length());
            //charAt() : 返回指定索引处的 char 值   ==》赋值给char字符对象ch
            char ch = string.charAt(index);
            // append(char c) :将 char 参数的字符串表示形式追加到此序列  ==》即将每次获取的ch值作拼接
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * @author yaoshibin
     * @date 2019/11/07
     * 生成合同编号
     * @return
     */
    public static String getContractNumber() {

        // LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//获取系统年月日
        String year = df.format(new Date()).trim().replace("-","");
        SimpleDateFormat d = new SimpleDateFormat("HH:mm:ss");//获取时分秒
        String hour = d.format(new Date()).trim().replace(":","");
        String contractNumber = "HT"+year+hour;

        return contractNumber;
    }

    public static String getResponse(String serverUrl) {
        //发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}