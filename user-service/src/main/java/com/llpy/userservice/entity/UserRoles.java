package com.llpy.userservice.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)  //开启链式编程
@ApiModel(value = "User对象", description = "权限角色")
public class UserRoles {
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    private Long userId;
    private Integer roleId;
}
