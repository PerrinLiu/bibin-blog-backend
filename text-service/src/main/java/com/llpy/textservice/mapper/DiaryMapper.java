package com.llpy.textservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.vo.DiaryVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {

    //获取全部日记
    IPage<DiaryVo> getList(Page<DiaryVo> diaryVoPage,Long userId, Integer status,String searchText);

    //获取单个用户日记
    List<DiaryVo> getListById(Long userId);

    //获取所有日记的基本信息
    IPage<DiaryVo> getListTitle(Page<DiaryVo> diaryVoPage, Long userId);

    //获取单个日记的信息
    DiaryVo getOneText(Long diaryId);
}
