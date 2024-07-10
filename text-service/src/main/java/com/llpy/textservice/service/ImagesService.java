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

    /**
     * 列表计数img
     *
     * @param pageSize   页面大小
     * @param pageNum    书籍页码
     * @param searchText 搜索文本
     * @return {@code Result<?>}
     */
    Result<?> listCountImg(Integer pageSize, Integer pageNum,String searchText);

    /**
     * 按用户列出img
     *
     * @param userId 用户id
     * @return {@code Result<?>}
     */
    Result<?> listImgByUser(Long userId);

    /**
     * 打开或关闭img
     *
     * @param imgId img id
     * @return {@code Result<?>}
     */
    Result<?> openOrCloseImg(Long imgId);

    /**
     * 删除img
     *
     * @param imgId img id
     * @return {@code Result<?>}
     */
    Result<?> deleteImg(Long imgId,Long userId);

}
