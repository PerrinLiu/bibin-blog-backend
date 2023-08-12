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

    Result<List<DiaryVo>> getList(Long userId);
}
