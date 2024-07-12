package com.llpy.userservice.controller;


import com.llpy.annotation.OperateLog;
import com.llpy.controller.BaseController;
import com.llpy.entity.UserDto;
import com.llpy.model.Result;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.dto.UserDto2;
import com.llpy.userservice.entity.dto.UserRegister;
import com.llpy.userservice.entity.vo.UserVo;
import com.llpy.userservice.redis.RedisService;
import com.llpy.userservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * 用户控制器
 *
 * @author LLPY
 * @date 2023/11/08
 */
@RestController
@Api(tags = {"用户控制类"})
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;

    private final RedisService redisService;

    public UserController(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    /**
     * 登录
     *
     * @param userLoginQuery 用户登录查询
     * @return {@link Result}<{@link UserDto}>
     */
    @PostMapping("/login")
    @OperateLog("登录")
    @ApiOperation(value = "登录")
    public Result<?> login(@RequestBody @Valid UserLoginQuery userLoginQuery, @RequestHeader("captchaToken") String captchaToken) {
        return userService.login(userLoginQuery, captchaToken);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return {@link Result}<{@link UserDto2}>
     */
    @GetMapping("/getUser")
    @ApiOperation(value = "获取用户信息")
    public Result<UserDto2> getUser() {
        return userService.getUser(loginUser().getUserId());
    }




    @GetMapping("/logout")
    @ApiOperation(value = "当前用户退出")
    public Result<?> logout() {
        return userService.logout(loginUser());
    }

    @PutMapping("/updateUser")
    @ApiOperation(value = "更新用户信息")
    public Result<UserDto2> updateUser(@RequestBody UserDto2 userDto2) {
        return userService.updateUser(userDto2);
    }

    @PostMapping("/updateUserImg")
    @ApiOperation(value = "更新用户头像")
    public Result<?> updateUserImg(@RequestBody MultipartFile file) {
        return userService.updateUserImg(file, loginUser().getUserId());
    }

    @PostMapping("/sendEmail")
    @ApiOperation(value = "发送邮件")
    @OperateLog("发送邮件")
    public Result<?> sendEmail(@RequestParam String email, @RequestParam String type,@RequestParam String message) {
        return userService.sendEmail(email, type,message);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册用户")
    @OperateLog("注册用户")
    public Result<?> register(@RequestBody UserRegister userRegister, @RequestHeader("email-token") String emailToken) {
        return userService.register(userRegister, emailToken);
    }

    @PostMapping("/emailIsTrue")
    @ApiOperation("修改密码验证邮箱验证码是否正确")
    public Result<?> emailIsTrue(@RequestBody UserRegister userRegister, @RequestHeader("email-token") String emailToken) {
        return userService.emailIsTure(userRegister, emailToken);
    }

    @PostMapping("/updatePassword")
    @ApiOperation("修改密码")
    @OperateLog("修改密码")
    public Result<?> updatePassword(@RequestBody UserDto2 userDto2) {
        return userService.updatePassword(userDto2);
    }


    @GetMapping("/common/getUserData")
    @ApiOperation("获取用户数据(头像和昵称")
    public HashMap<Long, UserVo> getUserData() {
        return redisService.getUserData();
    }


    @GetMapping("/getUserEmail")
    @ApiOperation("获取用户邮箱")
    public String getUserEmail(@RequestParam Long userId) {
        return userService.getUserEmail(userId);
    }


    @GetMapping("/getUserData")
    @ApiOperation("获取用户数据")
    public UserDto2 getUserData(@RequestParam Long userId) {
        return userService.getUser(userId).getData();
    }

}
