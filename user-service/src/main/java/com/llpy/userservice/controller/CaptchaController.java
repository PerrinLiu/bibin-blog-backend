package com.llpy.userservice.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.llpy.model.Result;
import com.llpy.utils.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * captcha控制器
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Controller
@RequestMapping("/user")
public class CaptchaController {

    private final DefaultKaptcha captchaProducer;

    private final RedisUtil redisUtil;

    public CaptchaController(DefaultKaptcha captchaProducer, RedisUtil redisUtil) {
        this.captchaProducer = captchaProducer;
        this.redisUtil = redisUtil;
    }


    @GetMapping("/captcha")
    public void generateCaptcha(HttpServletResponse response) throws IOException {
        // 生成验证码
        String captchaText = captchaProducer.createText();
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);

        // 将验证码图像发送给前端
        response.setContentType("image/jpeg");
        ImageIO.write(captchaImage, "jpg", response.getOutputStream());
    }

    @GetMapping("/generate-base64")
    @ResponseBody
    public Result<List<String>> generateCaptchaBase64(HttpServletRequest request) throws IOException {
        String captchaToken = request.getHeader("captchaToken");
        //如果有token
        if (captchaToken != null) {
            redisUtil.del(captchaToken);
        }
        // 生成验证码
        String captchaText = captchaProducer.createText();

        //生成一个不带‘ - ‘的uuid，用来和验证码一起存进redis
        String redisCaptchaKey = UUID.randomUUID().toString().replaceAll("-", "");

        redisUtil.set(redisCaptchaKey, captchaText, 1000 * 60 * 3);

        BufferedImage captchaImage = captchaProducer.createImage(captchaText);

        // 将验证码图像转换为Base64编码
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "jpg", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        //将base码和key一起返回
        List<String> list = new ArrayList<>();
        list.add(redisCaptchaKey);
        list.add(Base64.getEncoder().encodeToString(imageBytes));
        return Result.success(list);
    }
}
