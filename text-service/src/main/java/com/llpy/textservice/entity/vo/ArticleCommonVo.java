package com.llpy.textservice.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

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

    private String userId;

    private String userImg;

    private String userName;

    private String replyUserName;

    private String content;

    private String createTime;

    private Integer likeSum;

    private Boolean liked;

    private Boolean showDelete;

    private List<ArticleCommonVo> subComment;


}
