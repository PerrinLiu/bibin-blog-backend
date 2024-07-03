package com.llpy.textservice.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.textservice.entity.ArticleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.textservice.entity.vo.ArticleCommonVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LLPY
 * @date 2024/07/02
 * @since 2024-06-08
 */
@Mapper
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {

    /**
     * 获取一级评论
     *
     * @param articleId          文章id
     * @param articleCommentPage 文章评论页
     * @return {@code IPage<ArticleCommonVo>}
     */
    IPage<ArticleCommonVo> listCommentFinal(Long articleId, Page<ArticleCommonVo> articleCommentPage);

    /**
     * 获取一级评论的所有子级评论
     *
     * @param ids ids
     * @return {@code List<ArticleComment>}
     */
    List<ArticleComment> selectSubComment(List<Long> ids);

    /**
     * 获取评论的子级评论
     *
     * @param longs longs
     * @return {@code List<ArticleComment>}
     */
    List<ArticleComment> listCommentParent(List<Long> longs);

    /**
     * 删除最终id为commentId的评论
     *
     * @param commentId 评论id
     */
    void deleteCommentByFinalId(Long commentId);


    /**
     * 按父id删除评论
     *
     * @param commentId 评论id
     */
    void deleteCommentByParentId(Long commentId);

    /**
     * 获取文章评论数量
     *
     * @param articleId 文章id
     * @return {@code Integer}
     */
    Integer countByArticleId(Long articleId);

    /**
     * 按文章id删除评论
     *
     * @param articleId 文章id
     */
    void deleteByArticleId(Long articleId);
}
