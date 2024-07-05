package com.llpy.textservice.entity.vo;

import lombok.Data;

/**
 * 用户文章vo
 *
 * @author llpy
 * @date 2024/07/05
 */
@Data
public class UserArticleVo {

    /**
     * 喜欢
     */
    private int liked;
    /**
     * 点赞
     */
    private int star;
    /**
     * 组
     */
    private String groupList;
}
