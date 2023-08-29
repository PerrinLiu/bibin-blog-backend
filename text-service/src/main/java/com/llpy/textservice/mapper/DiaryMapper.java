package com.llpy.textservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.DiaryText;
import com.llpy.textservice.entity.dto.DiaryVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {

    //获取全部日记
    List<DiaryVo> getList();

    //获取单个用户日记
    List<DiaryVo> getListById(Long userId);

    //获取所有日记的基本信息
    List<DiaryVo> getListTitle(Long userId);

    //获取单个日记的信息
    DiaryVo getOneText(Long diaryId);
}
