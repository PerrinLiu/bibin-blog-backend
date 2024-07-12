package com.llpy.textservice.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 最近评论vo
 *
 * @author llpy
 * @date 2024/07/12
 */
@Data
public class RecentlyCommentVo {
    /**
     * 来自模块
     */
    private String articleTitle;

    private Long articleId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户img
     */
    private String userImg;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 所容纳之物
     */
    private String content;
    /**
     * 创造时间
     */
    private LocalDateTime createTime;
}
