package com.llpy.userservice.service;


import com.llpy.entity.UserDto;
import com.llpy.model.Result;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.dto.UserDto2;
import com.llpy.userservice.entity.dto.UserRegister;
import com.llpy.userservice.entity.vo.UserVo;
import io.swagger.models.auth.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务
 *
 * @author LLPY
 * @date 2023/11/08
 */
public interface UserService {
    /**
     * 登录
     *
     * @param userLoginQuery 用户登录查询
     * @param captcha        captcha
     * @return {@code Result<?>}
     */
    Result<?> login(UserLoginQuery userLoginQuery, String captcha);

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return {@code Result<UserDto2>}
     */
    Result<UserDto2> getUser(Long userId);

    /**
     * 注销
     *
     * @param loginUser 登录用户
     * @return {@code Result<?>}
     */
    Result<?> logout(UserDto loginUser);

    /**
     * 更新用户
     *
     * @param userDto2 用户dto2
     * @return {@code Result<UserDto2>}
     */
    Result<UserDto2> updateUser(UserDto2 userDto2);

    /**
     * 登记
     *
     * @param userRegister 用户寄存器
     * @param emailToken   电子邮件令牌
     * @return {@code Result<?>}
     */
    Result<?> register(UserRegister userRegister, String emailToken);

    /**
     * 更新用户img
     *
     * @param file   文件
     * @param userId 用户id
     * @return {@code Result<?>}
     */
    Result<?> updateUserImg(MultipartFile file, Long userId);

    /**
     * 发送电子邮件
     *
     * @param email   电子邮件
     * @param type    类型
     * @param message 消息
     * @return {@code Result<?>}
     */
    Result<?> sendEmail(String email, String type,String message);


    /**
     * 电子邮件是真
     *
     * @param userRegister 用户寄存器
     * @param emailToken   电子邮件令牌
     * @return {@code Result<?>}
     */
    Result<?> emailIsTure(UserRegister userRegister, String emailToken);


    /**
     * 更新密码
     *
     * @param userDto2 用户dto2
     * @return {@code Result<?>}
     */
    Result<?> updatePassword(UserDto2 userDto2);

    /**
     * 获取用户电子邮件
     *
     * @param userId 用户id
     * @return {@code String}
     */
    String getUserEmail(Long userId);
}
