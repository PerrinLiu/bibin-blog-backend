package com.llpy.textservice.mapper;

import com.llpy.textservice.entity.ArticleGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.textservice.entity.vo.ArticleGroupVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 文章分组表 Mapper 接口
 * </p>
 *
 * @author LLPY
 * @since 2024-06-08
 */
@Mapper
public interface ArticleGroupMapper extends BaseMapper<ArticleGroup> {

    int selectByName(String groupName);

}
