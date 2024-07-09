package com.llpy.textservice.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章公共vo
 *
 * @author llpy
 * @date 2024/06/30
 */
@Data
@Accessors(chain = true)
public class ArticleCommonVo {

    private Long id;

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
     * 回复用户名
     */
    private String replyNickname;

    /**
     * 所容纳之物
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 相同总和
     */
    private Integer likeSum;

    /**
     * 喜欢
     */
    private Boolean liked;

    /**
     * 显示删除
     */
    private Boolean showDelete;

    /**
     * 子评论
     */
    private List<ArticleCommonVo> subComment;


}
