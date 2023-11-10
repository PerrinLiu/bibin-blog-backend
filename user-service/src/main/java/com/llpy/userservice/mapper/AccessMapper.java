package com.llpy.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.entity.Access;
import org.apache.ibatis.annotations.Mapper;

/**
 * 访问映射器
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Mapper
public interface AccessMapper extends BaseMapper<Access> {
}
