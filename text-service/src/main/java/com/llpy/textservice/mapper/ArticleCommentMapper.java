package com.llpy.textservice.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.textservice.entity.Article;
import com.llpy.textservice.entity.ArticleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.textservice.entity.vo.ArticleCommonVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LLPY
 * @since 2024-06-08
 */
@Mapper
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {

    IPage<ArticleCommonVo> listComment(Long articleId, Page<ArticleCommonVo> articleCommentPage);

}
