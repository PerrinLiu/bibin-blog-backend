package com.llpy.textservice.service;


import com.llpy.model.Result;
import com.llpy.textservice.entity.dto.DiaryVo;

public interface DiaryService {
    Result addDiary(DiaryVo diaryVo,Long id);
}
