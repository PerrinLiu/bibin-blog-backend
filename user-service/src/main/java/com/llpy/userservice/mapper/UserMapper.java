package com.llpy.userservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> getUser();
}
