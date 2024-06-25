package com.llpy.textservice.service.impl;

import com.llpy.model.Result;
import com.llpy.textservice.service.ImagesService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 图像服务impl
 *
 * @author llpy
 * @date 2024/06/25
 */
public class ImagesServiceImpl implements ImagesService {

    @Override
    public Result<?> uploadFile(MultipartFile file) {
        //todo 添加文件唯一标志，按月划分文件
        String uploadDir = "/path/to/upload/dir";
        String serverUrl = "http://101.126.19.177:10010/text";
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        try {
            // 获取上传文件的名字
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + File.separator + fileName);
            // 确保上传目录存在
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // 保存文件到服务器目录
            Files.write(path, file.getBytes());

            // 生成文件的URL
            String fileUrl = serverUrl + "/files/common/" + fileName;

            // 返回文件URL
            return Result.success(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传文件失败");
        }
    }
}
