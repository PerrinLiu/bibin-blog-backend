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
    Result<?> uploadFile(MultipartFile file,Long userId);
}
