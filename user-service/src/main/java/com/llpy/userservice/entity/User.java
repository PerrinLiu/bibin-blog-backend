package com.llpy.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)  //开启链式编程
@ApiModel(value="User对象", description="用户表")
public class User {
    @TableId
    @ApiModelProperty(value = "用户id")
    private Long UserId;

    private String username;

    private Integer avatarId;

    private String nickname;

    private String password;

    private String email;

    private String gender;

    private String city;

    private Integer root;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;



}
