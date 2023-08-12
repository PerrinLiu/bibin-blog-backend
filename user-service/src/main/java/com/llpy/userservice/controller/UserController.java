package com.llpy.userservice.controller;


import com.llpy.controller.BaseController;
import com.llpy.entity.UserDto;
import com.llpy.model.Result;
import com.llpy.annotation.OperateLog;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.dto.UserDto2;
import com.llpy.userservice.entity.dto.UserRegister;
import com.llpy.userservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@Api(tags = {"用户控制类"})
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 登录
     *
     * @param userLoginQuery 用户登录查询
     * @return {@link Result}<{@link UserDto}>
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public Result<?> login(@RequestBody @Valid UserLoginQuery userLoginQuery){
        return userService.login(userLoginQuery);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return {@link Result}<{@link UserDto2}>
     */
    @GetMapping("/getUser")
    @OperateLog("获取用户信息")
    @ApiOperation(value = "获取用户信息")
    public Result<UserDto2> getUser(){
        return userService.getUser(loginUser().getUserId());
    }

    @GetMapping("/logout")
    @ApiOperation(value = "当前用户退出")
    public Result<?> logout(){
        return userService.logout(loginUser());
    }

    @PutMapping("/updateUser")
    @ApiOperation(value = "更新用户信息")
    public Result<UserDto2> updateUser(@RequestBody UserDto2 userDto2){
        return userService.updateUser(userDto2);
    }

    @PostMapping("/updateUserImg")
    @ApiOperation(value = "更新用户头像")
    public Result<?> updateUserImg(@RequestBody MultipartFile file) {
        return userService.updateUserImg(file,loginUser().getUserId());
    }

    @GetMapping("/sendEmail")
    @ApiOperation(value = "获得验证码")
    public Result<?> sendEmail(@RequestParam String email){
        return userService.sendEmail(email);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册用户")
    public Result<?> register(@RequestBody UserRegister userRegister, @RequestHeader("email-token")String emailToken){
        return userService.register(userRegister,emailToken);
    }

    @PutMapping("/updatePassword")
    @ApiOperation(value = "忘记密码")
    public Result<?> updatePassword(){
        return null;
    }
}
