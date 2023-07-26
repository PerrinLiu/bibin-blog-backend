package com.llpy.userservice.controller;

import com.llpy.controller.BaseController;
import com.llpy.entity.UserDto;
import com.llpy.model.Result;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.vo.UserDto2;
import com.llpy.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param userLoginQuery 用户登录查询
     * @return {@link Result}<{@link UserDto}>
     */
    @PostMapping("/login")
    public Result<UserDto> login(@RequestBody @Valid UserLoginQuery userLoginQuery){
        return userService.login(userLoginQuery);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return {@link Result}<{@link UserDto2}>
     */
    @GetMapping("/getUser")
    public Result<UserDto2> getUser(){
        return userService.getUser(loginUser().getUserId());
    }
}
