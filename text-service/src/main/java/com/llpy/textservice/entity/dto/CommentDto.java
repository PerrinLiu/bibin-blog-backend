package com.llpy.textservice.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 评论dto
 *
 * @author llpy
 * @date 2024/06/26
 */
@Data
@Accessors(chain = true)
public class CommentDto {

    @NotBlank(message = "评论内容不能为空")
    private String content;


    @NotBlank(message = "文章id不能为空")
    private Long articleId;


    private Long userId;


    private Long parentId;

}
