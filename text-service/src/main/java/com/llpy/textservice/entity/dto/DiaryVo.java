package com.llpy.textservice.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value="DiaryDto对象", description="日记返回信息")
public class DiaryVo {
    private Long diaryId;

    private Long userId;

    private String diaryTitle;

    private String diaryText;
}
