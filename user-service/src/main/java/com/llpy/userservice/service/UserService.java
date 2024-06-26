package com.llpy.userservice.service;


import com.llpy.entity.UserDto;
import com.llpy.model.Result;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.dto.UserDto2;
import com.llpy.userservice.entity.dto.UserRegister;
import io.swagger.models.auth.In;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author LLPY
 * @date 2023/11/08
 */
public interface UserService {
    Result<?> login(UserLoginQuery userLoginQuery, String captcha);

    Result<UserDto2> getUser(Long userId);

    Result<?> logout(UserDto loginUser);

    Result<UserDto2> updateUser(UserDto2 userDto2);

    Result<?> register(UserRegister userRegister, String emailToken);

    Result<?> updateUserImg(MultipartFile file, Long userId);

    Result<?> sendEmail(String email, String type);


    Result<?> emailIsTure(UserRegister userRegister, String emailToken);


    Result<?> updatePassword(UserDto2 userDto2);
}
