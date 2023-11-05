package com.llpy.userservice.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

public class CaptchaGenerator {
    public static void main(String[] args) {
        // 生成验证码
        String captchaText = generateCaptchaText(6); // 生成6位验证码
        BufferedImage captchaImage = generateCaptchaImage(captchaText);

        // 将验证码图像转换为字节数组或Base64编码
        byte[] imageBytes = imageToBytes(captchaImage);

        // 将验证码数据传递给前端（这里仅打印出字节数组长度，实际上需要通过HTTP响应发送给前端）
        System.out.println("验证码字节数组长度: " + imageBytes.length);
    }

    // 生成随机验证码文本
    private static String generateCaptchaText(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            captchaText.append(characters.charAt(index));
        }
        return captchaText.toString();
    }

    // 生成验证码图像
    private static BufferedImage generateCaptchaImage(String text) {
        int width = 200;
        int height = 80;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 绘制背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // 绘制文本
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString(text, 20, 50);

        // 添加干扰线等效果...

        g2d.dispose();
        return image;
    }

    // 将图像转换为字节数组
    private static byte[] imageToBytes(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
