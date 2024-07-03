package com.llpy.textservice.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.textservice.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.textservice.entity.vo.ArticleCountVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 获取文章列表
     *
     * @param articlePage 文章页面
     * @param searchText  搜索文本
     * @return {@code IPage<Article>}
     */
    IPage<Article> getArticleList(Page<Article> articlePage, String searchText);

    /**
     * 列出首页文章
     *
     * @return {@code List<Article>}
     */
    List<Article> listIndexArticle();

    /**
     * 评论计数加一
     *
     * @param articleId 文章id
     */
    void commentCountAddOne(Long articleId);

    /**
     * 评论数量减少一
     *
     * @param articleId 文章id
     */
    void commentCountReduceOne(Long articleId);

    /**
     * 更新评论计数
     *
     * @param articleId 文章id
     * @param sum       总和
     */
    void updateCommentCount(Long articleId, Integer sum);

    /**
     * 获取最近半年每天的文章数
     *
     * @return {@code Integer[]}
     */
    List<ArticleCountVo> selectCountByDate();
}
