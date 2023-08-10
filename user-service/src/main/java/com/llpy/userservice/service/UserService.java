package com.llpy.userservice.service;


import com.llpy.entity.UserDto;
import com.llpy.model.Result;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.dto.UserDto2;
import com.llpy.userservice.entity.dto.UserRegister;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Result<UserDto> login(UserLoginQuery userLoginQuery);

    Result<UserDto2> getUser(Long userId);

    Result<?> logout(UserDto loginUser);

    Result<UserDto2> updateUser(UserDto2 userDto2);

    Result<?> register(UserRegister userRegister,String emailToken);

    Result<?> updateUserImg(MultipartFile file, Long userId);

    Result<?> sendEmail(String email);
}
