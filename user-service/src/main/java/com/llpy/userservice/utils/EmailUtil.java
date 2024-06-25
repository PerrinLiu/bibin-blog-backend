package com.llpy.userservice.utils;

import com.llpy.userservice.entity.dto.MailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.InternetAddress;
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

    public MailDto sendMail(MailDto mailVo, String message) {
        try {
            if (StringUtils.isEmpty(mailVo.getTo())) {
                throw new RuntimeException("邮件收信人不能为空");
            }

            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            // 获取配置属性
            String from = mailSender.getJavaMailProperties().getProperty("from");
            mailVo.setFrom(from);

            // 设置发件人
            messageHelper.setFrom(new InternetAddress(from, "Bibin", "UTF-8"));

            // 设置收件人
            messageHelper.setTo(mailVo.getTo().split(","));

            // 设置主题
            messageHelper.setSubject("Bibin邮件通知");

            // 生成验证码
            String code = generateRandomCode();
            String date = LocalDateTime.now().toString();
            // 设置HTML内容，包含样式
            String htmlContent = "<html>\n" +
                    "<body style=\"font-family: Arial, sans-serif; margin: 20px; padding: 0; background-color: #f4f4f4;\">\n" +
                    "        <tr>\n" +
                    "            <td align=\"center\" style=\"padding: 20px;\">\n" +
                    "                <table role=\"presentation\" style=\"width: 600px; border-collapse: collapse; background-color: white; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">\n" +
                    "                    <tr>\n" +
                    "                        <td style=\"padding: 20px;\">\n" +
                    "                            <h2 style=\"color: #333;\">您有一条来自Bibin的消息~</h2>\n" +
                    "                            <p style=\"color: #555; line-height: 1.6;\">感谢使用Bibin!以下是邮件的正文内容。</p>\n" +
                    "                            <p style=\"color: #555; line-height: 1.6;\">正在进行bibin账号的<span style='font-size: 18px; font-weight: bold;'>"+ message + "</span>，您的验证码是：<span style='font-size: 20px; font-weight: bold;'>"+ code + "</span></p>\n" +
                    "                            <p style=\"color: #555;\">此验证码将在5分钟内有效。</p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td style=\"padding: 20px; background-color: #f4f4f4; text-align: center; color: #777;\">\n" +
                    "                            <p style=\"margin: 0;\">&copy;"+date+ "&nbsp;&nbsp;Bibin. 保留所有权利。</p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n";

            messageHelper.setText(htmlContent, true); // 设置为true，表示内容是HTML格式

            // 更新MailDto中的信息
            mailVo.setText(code);
            mailVo.setSentDate(LocalDateTime.now());

            // 发送邮件
            mailSender.send(messageHelper.getMimeMessage());

            // 更新邮件状态
            mailVo.setStatus("ok");
            log.info("发送邮件成功：{} -> {}", mailVo.getFrom(), mailVo.getTo());

            return mailVo;
        } catch (Exception e) {
            log.error("发送邮件失败:", e);
            mailVo.setStatus("fail");
            mailVo.setError(e.getMessage());
            return mailVo;
        }
    }

    // 生成随机验证码方法
    public static String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成6位随机数
        return Integer.toString(code);
    }
}

