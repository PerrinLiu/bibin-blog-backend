package com.llpy.textservice.service.Impl;

import com.llpy.model.Result;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.DiaryText;
import com.llpy.textservice.entity.dto.DiaryVo;
import com.llpy.textservice.mapper.DiaryMapper;
import com.llpy.textservice.mapper.DiaryTextMapper;
import com.llpy.textservice.service.DiaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DiaryServiceImpl implements DiaryService {
    private final DiaryMapper diaryMapper;
    private final DiaryTextMapper diaryTextMapper;

    public DiaryServiceImpl(DiaryMapper diaryMapper, DiaryTextMapper diaryTextMapper) {
        this.diaryMapper = diaryMapper;
        this.diaryTextMapper = diaryTextMapper;
    }

    @Override
    @Transactional  //开启事务，确保同时成功，或同时失败，保持数据一致性
    public Result addDiary(DiaryVo diaryVo,Long id) {
        if(id ==null) return Result.success("用户登录过期");
        //新建日记信息对象
        DiaryText diaryText = new DiaryText();
        diaryText.setDiaryTextId(null).setDiaryId(null)
                .setDiaryTitle(diaryVo.getDiaryTitle())
                .setDiaryText(diaryVo.getDiaryText())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        diaryTextMapper.insert(diaryText);
        //插入后获得生成的日记信息id
        Long textId = diaryText.getDiaryTextId();

        //添加id到日记基本信息表，然后插入数据库
        Diary diary = new Diary();
        diary.setUserId(id)
                .setDiaryId(null)
                .setDiaryTextId(textId)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        diaryMapper.insert(diary);
        //日记id
        Long diaryId = diary.getDiaryId();

        //更新日记信息的日记id
        diaryText.setDiaryId(diaryId);
        diaryTextMapper.updateById(diaryText);

        return Result.success();
    }
}
