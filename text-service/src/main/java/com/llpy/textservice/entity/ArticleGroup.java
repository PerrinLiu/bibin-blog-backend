package com.llpy.textservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章分组表
 * </p>
 *
 * @author LLPY
 * @since 2024-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ArticleGroup对象", description="文章分组表")
public class ArticleGroup extends Model<ArticleGroup> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分组ID，自增主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文章类型")
    private String articleType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
