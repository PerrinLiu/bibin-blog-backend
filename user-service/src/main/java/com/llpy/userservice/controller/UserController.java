package com.llpy.userservice.controller;

import com.llpy.model.Result;
import com.llpy.userservice.entity.User;
import com.llpy.userservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/login")
    public Result login(){
        List<User> user = userMapper.getUser();
        return Result.success(user);
    }
}
