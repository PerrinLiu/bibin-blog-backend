package com.llpy.textservice.service;

import com.llpy.model.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图像服务
 *
 * @author llpy
 * @date 2024/06/25
 */
public interface ImagesService {
    /**
     * 上传img
     *
     * @param file   文件
     * @param userId 用户id
     * @return {@code Result<?>}
     */
    Result<?> uploadImg(MultipartFile file,Long userId);

    /**
     * 按用户id列出img
     *
     * @param userId 用户id
     * @return {@code Result<?>}
     */
    Result<?> listImgByUserId(Long userId);
}
