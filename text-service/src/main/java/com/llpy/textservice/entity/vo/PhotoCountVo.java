package com.llpy.textservice.entity.vo;

import lombok.Data;

/**
 * 照片计数vo
 *
 * @author llpy
 * @date 2024/07/09
 */
@Data
public class PhotoCountVo {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String nickname;
    /**
     * 用户img
     */
    private String userImg;

    /**
     * 照片计数
     */
    private Integer photoCount;

}
