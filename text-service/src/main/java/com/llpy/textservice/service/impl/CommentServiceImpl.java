package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Article;
import com.llpy.textservice.entity.ArticleComment;
import com.llpy.textservice.entity.dto.CommentDto;
import com.llpy.textservice.mapper.ArticleCommentMapper;
import com.llpy.textservice.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * 评论服务impl
 *
 * @author llpy
 * @date 2024/06/26
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final ArticleCommentMapper articleCommentMapper;

    public CommentServiceImpl(ArticleCommentMapper articleCommentMapper) {
        this.articleCommentMapper = articleCommentMapper;
    }

    @Override
    public Result<?> listComment(Long articleId, Integer pageSize, Integer pageNum) {
        // TODO: 2024/6/27 需要返回用户头像和用户id
        Page<ArticleComment> articleCommentPage = new Page<>(pageNum, pageSize);
        IPage<Article> articlePage = articleCommentMapper.listComment(articleId, articleCommentPage);
        return Result.success(articlePage.getRecords());
    }

    @Override
    public Result<?> addComment(CommentDto commentDto) {
        ArticleComment articleComment = new ArticleComment();
        articleComment.setContent(commentDto.getContent());
        articleComment.setArticleId(commentDto.getArticleId());
        articleComment.setUserId(commentDto.getUserId());
        if(null != commentDto.getParentId()) {
            articleComment.setParentId(commentDto.getParentId());
        }
        articleCommentMapper.insert(articleComment);
        return Result.success();
    }
}
