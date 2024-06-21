package com.llpy.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 菜单vo
 *
 * @author llpy
 * @date 2024/06/21
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "权限菜单", description = "用户的权限")
public class MenuVo {

    @TableId
    private Long permissionId;

    @ApiModelProperty(value = "权限名称")
    private String permissionName;

}
