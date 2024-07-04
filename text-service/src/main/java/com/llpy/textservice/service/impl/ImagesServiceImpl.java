package com.llpy.textservice.service.impl;

import com.llpy.model.Result;
import com.llpy.textservice.service.ImagesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 图像服务impl
 *
 * @author llpy
 * @date 2024/06/25
 */
@Service
public class ImagesServiceImpl implements ImagesService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server.url}")
    private String serverUrl;

    @Override
    public Result<?> uploadFile(MultipartFile file,Long userId) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        try {
            // 获取当前日期并格式化为 "yyyy-MM" 格式
            String currentMonth ="/"+ userId + "/"+new SimpleDateFormat("yyyy-MM").format(new Date());

            // 获取上传文件的名字,生成唯一的名称
            String fileName = file.getOriginalFilename();
            String uniqueFileName = getUniqueFileName(fileName);
            Path path = Paths.get(uploadDir+currentMonth + File.separator + uniqueFileName);


            // 确保上传目录存在
            Path uploadPath = Paths.get(uploadDir+currentMonth);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // 保存文件到服务器目录
            Files.write(path, file.getBytes());

            // 生成文件的URL
            String fileUrl = serverUrl + "/files" +currentMonth+"/"+ uniqueFileName;

            // 返回文件URL
            return Result.success(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传文件失败");
        }
    }

    private static String getUniqueFileName(String fileName) {
        String fileExtension = "";

        // 提取文件扩展名
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
        }

        // 生成唯一的文件名
        return UUID.randomUUID().toString().replace("-", "") + fileExtension;
    }
}
