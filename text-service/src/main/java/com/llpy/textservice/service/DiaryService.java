package com.llpy.textservice.service;


import com.llpy.model.Result;
import com.llpy.textservice.entity.dto.DiaryVo;

import java.util.List;

public interface DiaryService {
    Result addDiary(DiaryVo diaryVo,Long id);

    Result<List<DiaryVo>> getDiary(Long userId);
}
