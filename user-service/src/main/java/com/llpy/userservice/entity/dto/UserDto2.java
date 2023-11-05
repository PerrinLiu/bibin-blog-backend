package com.llpy.userservice.entity.dto;


import com.baomidou.mybatisplus.annotation.TableId;
import com.llpy.entity.MenuVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value="UserDto对象", description="用户Dto表")
public class UserDto2 implements Serializable {

    @TableId
    @ApiModelProperty(value = "用户id")
    private Long userId;

    private String username;

    private String password;

    private String userImg;

    private String nickname;

    private String email;

    private String gender;

    private String city;

    private String roleName;

    private List<MenuVo> menuVos;

}

