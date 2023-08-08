package com.llpy.textservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value="DiaryText对象", description="日记信息表")
public class DiaryText {
    @TableId
    private Long diaryTextId;

    private Long diaryId;

    private String diaryTitle;

    private String diaryText;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
