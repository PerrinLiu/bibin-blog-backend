package com.llpy.textservice.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章详细信息vo
 *
 * @author llpy
 * @date 2024/06/12
 */
@Data
public class ArticleDetailsVo {

    @ApiModelProperty(value = "文章ID，自增主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "文章内容id")
    private String articleText;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章封面")
    private String cover;

    @ApiModelProperty(value = "文章描述")
    private String des;

    @ApiModelProperty(value = "分组ID，关联到文章分组表中的分组ID")
    private String articleGroupId;

    @ApiModelProperty(value = "评论数")
    private Integer commentSum;

    @ApiModelProperty(value = "阅读量")
    private Integer readSum;

    @ApiModelProperty(value = "收藏数")
    private Integer collectionsSum;

    @ApiModelProperty(value = "点赞数")
    private Integer likeSum;

    @ApiModelProperty(value = "创建人")
    private Long creatBy;

    @ApiModelProperty(value = "是否点赞")
    private boolean liked;

    @ApiModelProperty(value = "是否收藏")
    private boolean star;

    @ApiModelProperty(value = "文章创建时间，默认为当前时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
