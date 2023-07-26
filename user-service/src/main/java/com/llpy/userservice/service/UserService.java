package com.llpy.userservice.service;


import com.llpy.entity.UserDto;
import com.llpy.model.Result;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.vo.UserDto2;

public interface UserService {
    Result<UserDto> login(UserLoginQuery userLoginQuery);

    Result<UserDto2> getUser(Integer userId);
}
