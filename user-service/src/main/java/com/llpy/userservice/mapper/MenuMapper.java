package com.llpy.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.entity.MenuVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<MenuVo> {
    List<MenuVo> getUserRoot(Long userId);
}
