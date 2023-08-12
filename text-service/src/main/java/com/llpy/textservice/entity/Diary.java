package com.llpy.textservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)  //开启链式编程
@ApiModel(value="Diary对象", description="日记表")
public class Diary {
    @TableId
    private Long diaryId;

    private Long userId;

    private String diaryTitle;

    private Long diaryTextId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
