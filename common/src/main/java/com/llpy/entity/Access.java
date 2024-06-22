package com.llpy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 通道
 *
 * @author llpy
 * @date 2024/06/21
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "IP地址表", description = "访问量统计")
public class Access {
    @TableId
    private Long id;

    private String accessName;

    private String ip;

    private Integer count;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
