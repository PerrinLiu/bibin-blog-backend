package com.llpy.textservice.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)  //开启链式编程
@ApiModel(value = "Article", description = "文章表")
public class Article {
    @TableId
    private Long articleId;

    private String text;
}
