package com.llpy.textservice.entity.dto;

import lombok.Data;

/**
 * 评估日记dto
 *
 * @author llpy
 * @date 2024/07/04
 */
@Data
public class AssessDiaryDto {

    /**
     * 日记id
     */
    private Long diaryId;

    /**
     * 拒绝理由
     */
    private String rejectReason;
}
