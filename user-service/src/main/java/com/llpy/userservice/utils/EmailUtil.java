package com.llpy.userservice.utils;

import com.llpy.userservice.entity.dto.MailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 有用电子邮件
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Component
@Service
@Slf4j
public class EmailUtil {
    private final JavaMailSenderImpl mailSender;


    public EmailUtil(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }


    public MailDto sendMail(MailDto mailVo) {
        try {
            if (StringUtils.isEmpty(mailVo.getTo())) {
                throw new RuntimeException("邮件收信人不能为空");
            }

            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            //拿配置属性
            String from = mailSender.getJavaMailProperties().getProperty("from");
            mailVo.setFrom(from);
            //发送人
            messageHelper.setFrom(mailVo.getFrom());
            //接收人
            messageHelper.setTo(mailVo.getTo().split(","));
            //主题
            messageHelper.setSubject("验证码");
            //生成验证码
            String s = generateRandomCode();
            messageHelper.setText(s);

            mailVo.setText(s);
            mailVo.setSentDate(LocalDateTime.now());

            mailSender.send(messageHelper.getMimeMessage());
            mailVo.setStatus("ok");
            log.info("发送邮件成功：{}->{}", mailVo.getFrom(), mailVo.getTo());

            return mailVo;
        } catch (Exception e) {
            log.error("发送邮件失败:", e);
            mailVo.setStatus("fail");
            mailVo.setError(e.getMessage());
            return mailVo;
        }

    }

    public static String generateRandomCode() {
        // 生成随机验证码逻辑
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成6位随机数
        return Integer.toString(code);
    }
}


//package com.llpy.userservice.utils;
//
//        import com.llpy.userservice.entity.dto.MailDto;
//        import lombok.extern.slf4j.Slf4j;
//        import org.springframework.mail.javamail.JavaMailSenderImpl;
//        import org.springframework.mail.javamail.MimeMessageHelper;
//        import org.springframework.stereotype.Component;
//        import org.springframework.stereotype.Service;
//        import org.springframework.util.StringUtils;
//
//        import java.time.LocalDateTime;
//        import java.util.Random;

///**
// * 有用电子邮件
// *
// * @author LLPY
// * @date 2023/11/08
// */
//@Component
//@Service
//@Slf4j
//public class EmailUtil {
//    private final JavaMailSenderImpl mailSender;
//
//    public EmailUtil(JavaMailSenderImpl mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public MailDto sendMail(MailDto mailVo) {
//        try {
//            if (StringUtils.isEmpty(mailVo.getTo())) {
//                throw new RuntimeException("邮件收信人不能为空");
//            }
//
//            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
//            // 获取配置属性
//            String from = mailSender.getJavaMailProperties().getProperty("from");
//            mailVo.setFrom(from);
//
//            // 设置发件人
//            messageHelper.setFrom(mailVo.getFrom());
//
//            // 设置收件人
//            messageHelper.setTo(mailVo.getTo().split(","));
//
//            // 设置主题
//            messageHelper.setSubject("验证码邮件");
//
//            // 生成验证码
//            String code = generateRandomCode();
//
//            // 设置HTML内容，包含样式
//            String htmlContent = "<html><body><h1 style=\"color: #3e8e41;\">验证码邮件</h1>"
//                    + "<p>你的验证码是：<span style=\"font-size: 20px; font-weight: bold;\">" + code + "</span></p>"
//                    + "<p style=\"color: #555;\">此验证码将在10分钟内有效。</p></body></html>";
//
//            messageHelper.setText(htmlContent, true); // 设置为true，表示内容是HTML格式
//
//            // 更新MailDto中的信息
//            mailVo.setText(htmlContent);
//            mailVo.setSentDate(LocalDateTime.now());
//
//            // 发送邮件
//            mailSender.send(messageHelper.getMimeMessage());
//
//            // 更新邮件状态
//            mailVo.setStatus("ok");
//            log.info("发送邮件成功：{} -> {}", mailVo.getFrom(), mailVo.getTo());
//
//            return mailVo;
//        } catch (Exception e) {
//            log.error("发送邮件失败:", e);
//            mailVo.setStatus("fail");
//            mailVo.setError(e.getMessage());
//            return mailVo;
//        }
//    }
//
//    // 生成随机验证码方法
//    public static String generateRandomCode() {
//        Random random = new Random();
//        int code = 100000 + random.nextInt(900000); // 生成6位随机数
//        return Integer.toString(code);
//    }
//}

