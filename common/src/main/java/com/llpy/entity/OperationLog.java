package com.llpy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author llpy
 * @date 2024/06/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OperationLog对象", description = "操作日志记录表")
public class OperationLog {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作日志id")
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "日志名称")
    private String logName;

    @ApiModelProperty(value = "操作用户id")
    private Long userId;

    @ApiModelProperty(value = "录入时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "请求路径")
    private String request;

    @ApiModelProperty(value = "响应大小")
    private Integer responseSize;

    @ApiModelProperty(value = "网址")
    private String referrer;

    @ApiModelProperty(value = "操作人ip")
    private String host;

    @ApiModelProperty(value = "被操作对象")
    private String beOperated;

    @ApiModelProperty(value = "日志备注")
    private String logDesc;

    @ApiModelProperty(value = "数据参数")
    private String params;


}