package com.llpy.textservice.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.textservice.entity.PhotoWall;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.textservice.entity.vo.PhotoCountVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 照片墙 Mapper 接口
 * </p>
 *
 * @author LLPY
 * @since 2024-07-09
 */
@Mapper
public interface PhotoWallMapper extends BaseMapper<PhotoWall> {

    List<PhotoWall> listImgByUserId(Long userId);

    IPage<PhotoCountVo> listCountImg(Page<PhotoCountVo> page, String searchText);

    void updateImgToPrivate(List<String> strings,Long userId);

    List<PhotoWall> listImgByUser(Long userId);
}
