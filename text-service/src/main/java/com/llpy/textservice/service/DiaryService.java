package com.llpy.textservice.service;


import com.llpy.model.Result;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.dto.DiaryVo;

import java.util.List;

public interface DiaryService {
    Result addDiary(DiaryVo diaryVo,Long id);

    Result<List<DiaryVo>> getDiary();

    Result<List<DiaryVo>> getDiary(Long userId);

    Result<List<Diary>> getDiaryBase(Long userId);

    Result<DiaryVo> getOneText(Long diaryId);

    Result deleteDiaryOne(Long diaryId);
}
