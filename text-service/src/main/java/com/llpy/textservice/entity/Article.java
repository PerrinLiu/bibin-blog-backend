package com.llpy.textservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author HXB
 * @since 2024-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Article对象", description = "文章表")
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID，自增主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "文章内容id")
    private Long articleTextId;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "分组ID，关联到文章分组表中的分组ID")
    private Long articleGroupId;

    @ApiModelProperty(value = "评论数")
    private Integer commentSum;

    @ApiModelProperty(value = "阅读量")
    private Integer readSum;

    @ApiModelProperty(value = "收藏数")
    private String collectionsSum;

    @ApiModelProperty(value = "点赞数")
    private Integer likeSum;

    @ApiModelProperty(value = "创建人")
    private Long creatBy;

    @ApiModelProperty(value = "文章创建时间，默认为当前时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
