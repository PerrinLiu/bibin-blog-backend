package com.llpy.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationMapper extends BaseMapper<OperationLog> {
}
