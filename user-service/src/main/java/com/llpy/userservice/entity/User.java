package com.llpy.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 使用者
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Data
@Accessors(chain = true)  //开启链式编程
@ApiModel(value = "User对象", description = "用户表")
public class User {
    @TableId
    @ApiModelProperty(value = "用户id")
    private Long userId;

    private String username;

    private String userImg;

    private String nickname;

    private String password;

    private String email;

    private String gender;

    private String city;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
