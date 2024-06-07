package com.llpy.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.llpy.userservice.entity.UserRoles;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户角色映射器
 *
 * @author 黄晓冰
 * @date 2024/02/26
 */
@Mapper
public interface UserRolesMapper extends BaseMapper<UserRoles> {

}
