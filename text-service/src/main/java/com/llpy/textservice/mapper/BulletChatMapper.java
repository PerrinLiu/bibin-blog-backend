package com.llpy.textservice.mapper;

import com.llpy.textservice.entity.BulletChat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.textservice.entity.vo.BulletChatVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 弹幕表 Mapper 接口
 * </p>
 *
 * @author LLPY
 * @since 2024-07-12
 */
@Mapper
public interface BulletChatMapper extends BaseMapper<BulletChat> {

    List<BulletChatVo> getBulletChat();


}
