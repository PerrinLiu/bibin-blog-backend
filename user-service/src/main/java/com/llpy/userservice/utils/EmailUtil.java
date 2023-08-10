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
