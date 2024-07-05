package com.llpy.textservice.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 推荐文章vo
 *
 * @author llpy
 * @date 2024/07/05
 */
@Data
@Accessors(chain = true)
public class RecommendArticleVo {
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章描述")
    private String des;
}
