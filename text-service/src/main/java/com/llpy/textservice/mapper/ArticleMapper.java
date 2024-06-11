package com.llpy.textservice.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.textservice.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author HXB
 * @since 2024-06-08
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    IPage<Article> getArticleList(Page<Article> articlePage, String searchText);
}
