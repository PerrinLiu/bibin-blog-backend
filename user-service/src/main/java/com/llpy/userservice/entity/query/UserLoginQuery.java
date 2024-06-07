package com.llpy.userservice.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author LLPY
 * @date 2021-03-24
 * @return
 * @description
 */
@Data
@ApiModel(value = "用户登录接收信息实体")
public class UserLoginQuery {

    @ApiModelProperty(value = "登录账号", name = "username", required = true)
    private String username;

    @ApiModelProperty(value = "用户密码", name = "password", required = true)
    @NotBlank(message = "密码不能为空")
    @NotEmpty(message = "密码不能为空")
    @NotNull(message = "密码不能为NULL")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String captcha;
}
