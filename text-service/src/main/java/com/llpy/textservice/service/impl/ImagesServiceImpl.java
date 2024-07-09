package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.enums.CommonEnum;
import com.llpy.model.Result;
import com.llpy.textservice.entity.PhotoWall;
import com.llpy.textservice.entity.vo.PhotoCountVo;
import com.llpy.textservice.feign.UserService;
import com.llpy.textservice.feign.entity.UserDto2;
import com.llpy.textservice.mapper.PhotoWallMapper;
import com.llpy.textservice.service.ImagesService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoWallMapper photoWallMapper;

    @Override
    public Result<?> uploadImg(MultipartFile file,Long userId) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }
        //如果不是图片，不给上传
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            return Result.error("文件格式不正确");
        }

        //获取用户大小限制
        UserDto2 user = userService.getUser(userId);
        long maxSize = 0L;
        for (CommonEnum value : CommonEnum.values()) {
            if(value.getKey().equals(user.getRoleName())){
                maxSize = value.getMaxSize();
            }
        }
        //用户文件夹
        String userPath = uploadDir + "/" + userId +"/";
        // 当前月份的文件夹，获取当前日期并格式化为 "yyyy-MM" 格式
        String folderByMonth = new SimpleDateFormat("yyyy-MM").format(new Date());
        String userPathCurrentMonth =userPath+folderByMonth;
        // 获取上传文件的名字,生成唯一的名称
        String fileName = file.getOriginalFilename();
        String uniqueFileName = getUniqueFileName(fileName);
        try {
            Path path = Paths.get(userPathCurrentMonth + File.separator + uniqueFileName);
            // 检查用户文件夹大小
            Path userFolder = Paths.get(userPath);
            long folderSize = getFolderSize(userFolder);
            if (folderSize + file.getSize() > maxSize) {
                return Result.error("您的剩余空间" + (maxSize - folderSize) / 1024 / 1024 +"MB,不足以上传该文件,请联系管理员扩容空间");
            }


            // 确保上传目录存在
            Path uploadPath = Paths.get(userPathCurrentMonth);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // 保存文件到服务器目录
            Files.write(path, file.getBytes());

            // 压缩成缩略图并保存
            // 生成缩略图
            File thumbnailFile = new File(userPathCurrentMonth+ File.separator +  "thumbnail_" + uniqueFileName);
            Thumbnails.of(path.toFile())
                    .size(320, 180)
                    .toFile(thumbnailFile);

            // 可访问的路径
            String userPathCurrentMonthUrl = serverUrl + "/files" + "/" + userId +"/" +folderByMonth + "/";
            // 生成文件的URL
            String fileUrl = userPathCurrentMonthUrl+ uniqueFileName;
            // 生成缩略图的URL
            String thumbnailUrl = userPathCurrentMonthUrl + "thumbnail_" + uniqueFileName;

            // 保存到数据库
            PhotoWall photoWall = new PhotoWall();
            photoWall.setUserId(userId).setImgUrl(fileUrl).setThumbnailImgUrl(thumbnailUrl);
            photoWallMapper.insert(photoWall);
            // 返回文件URL
            return Result.success(fileUrl);

        } catch (Exception e) {
            //发送异常删除上传的图片
            File file1 = new File(userPathCurrentMonth + File.separator + uniqueFileName);
            if (file1.exists()) {
                file1.delete();
            }
            File file2 = new File(userPathCurrentMonth + File.separator + "thumbnail_" + uniqueFileName);
            if (file2.exists()) {
                file2.delete();
            }
            return Result.error("上传文件失败");
        }
    }

    @Override
    public Result<?> listImgByUserId(Long userId) {
        List<PhotoWall> res =photoWallMapper.listImgByUserId(userId);
        return Result.success(res);
    }

    @Override
    public Result<?> listCountImg(Integer pageSize, Integer pageNum,String searchText) {
        Page<PhotoCountVo> page = new Page<>(pageNum,pageSize);
        IPage<PhotoCountVo> res = photoWallMapper.listCountImg(page,searchText.trim());
        return Result.success(res);
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

    /**
     * 获取文件夹大小
     *
     * @param folder 文件夹
     * @return long
     */
    public long getFolderSize(Path folder) {
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            return 0;
        }
        try {
            return Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
        } catch (IOException e) {
            throw new RuntimeException("获取文件夹大小失败");
        }
    }

}
