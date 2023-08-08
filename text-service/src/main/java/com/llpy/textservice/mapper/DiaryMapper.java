package com.llpy.textservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.DiaryText;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {

}
