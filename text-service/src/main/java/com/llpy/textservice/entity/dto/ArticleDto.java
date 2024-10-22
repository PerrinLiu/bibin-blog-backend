package com.llpy.textservice.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 文章dto
 *
 * @author llpy
 * @date 2024/06/08
 */
@Data
public class ArticleDto {
    /**
     * 文章id
     */
    private Long articleId;

    @NotBlank(message = "文章标题不能为空")
    private String title;

    /**
     * 文章封面
     */
    @NotBlank(message = "文章封面不能为空")
    private String cover;

    /**
     * 文章正文
     */
    @NotBlank(message = "文章正文不能为空")
    private String articleText;

    /**
     * 文章描述
     */
    private String des;

    /**
     * 组id
     */
    private List<Long> groupIds;
}
