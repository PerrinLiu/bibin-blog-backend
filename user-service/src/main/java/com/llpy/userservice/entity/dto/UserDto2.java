package com.llpy.userservice.entity.dto;


import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="UserDto对象", description="用户Dto表")
public class UserDto2 implements Serializable {

    @TableId
    @ApiModelProperty(value = "用户id")
    private Long userId;

    private String username;

    private String userImg;

    private String nickname;

    private String email;

    private String gender;

    private String city;

    private Integer root;

}

