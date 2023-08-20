package com.llpy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value="IP地址表", description="访问量统计")
public class Access {
    @TableId
    private Long id;

    private String accessName;

    private String ip;
}
