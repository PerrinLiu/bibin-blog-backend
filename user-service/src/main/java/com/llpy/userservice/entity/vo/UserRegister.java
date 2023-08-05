package com.llpy.userservice.entity.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@Api(tags = {"用户控制类"})
@Data
public class UserRegister {

    @ApiModelProperty(value = "用户名", name = "username", required = true)
    private String username;


    @ApiModelProperty(value = "用户密码", name = "password", required = true)
    @NotBlank(message = "密码不能为空")
    @NotEmpty(message = "密码不能为空")
    @NotNull(message = "密码不能为NULL")
    private String password;

    @ApiModelProperty(value = "昵称", name = "nickname", required = true)
    private  String nickname;

    @ApiModelProperty(value = "验证码")
    private String captcha;
}
