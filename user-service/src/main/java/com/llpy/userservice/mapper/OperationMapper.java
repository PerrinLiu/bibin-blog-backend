package com.llpy.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作映射器
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Mapper
public interface OperationMapper extends BaseMapper<OperationLog> {
}
