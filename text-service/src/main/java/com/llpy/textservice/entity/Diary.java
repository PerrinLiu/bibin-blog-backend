package com.llpy.textservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 记事簿
 *
 * @author llpy
 * @date 2024/06/22
 */
@Data
@Accessors(chain = true)  //开启链式编程
@ApiModel(value="Diary对象", description="日记表")
public class Diary extends Model<Diary> {
    @TableId
    private Long diaryId;

    private Long userId;

    private String diaryTitle;

    private Long diaryTextId;

    private int isOpen;

    /**
     * 日记状态（1:未审核 2:通过 3:拒绝）
     */
    private int diaryStatus;

    /**
     * 审核人
     */
    private Long passUser;

    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
