package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Article;
import com.llpy.textservice.entity.ArticleComment;
import com.llpy.textservice.entity.dto.CommentDto;
import com.llpy.textservice.entity.vo.ArticleCommonVo;
import com.llpy.textservice.feign.UserService;
import com.llpy.textservice.feign.entity.UserVo;
import com.llpy.textservice.mapper.ArticleCommentMapper;
import com.llpy.textservice.service.CommentService;
import com.llpy.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论服务impl
 *
 * @author llpy
 * @date 2024/06/26
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final ArticleCommentMapper articleCommentMapper;

    @Autowired
    private UserService userService;

    public CommentServiceImpl(ArticleCommentMapper articleCommentMapper) {
        this.articleCommentMapper = articleCommentMapper;
    }

    @Override
    public Result<?> listComment(Long articleId, Integer pageSize, Integer pageNum) {
        HashMap<Long, UserVo> userData = userService.getUserData();
        // TODO: 2024/6/27 待划分子级和父级
        Page<ArticleCommonVo> articleCommentPage = new Page<>(pageNum, pageSize);
        IPage<ArticleCommonVo> articlePage = articleCommentMapper.listComment(articleId, articleCommentPage);
        List<ArticleCommonVo> records = articlePage.getRecords();
        //构造评论数据
        for (ArticleCommonVo record : records) {
            //父级评论
            Long userId = Long.parseLong(record.getUserId());
            record.setUserImg(userData.get(userId).getUserImg());
            record.setUserName(userData.get(userId).getUserName());
            record.setLiked(false).setShowDelete(false);
            //子级评论
            List<ArticleCommonVo> subComment = new ArrayList<>();
            Long id = record.getId();
            record.setSubComment(subComment);
        }
        return Result.success(records);
    }

    /**
     * 添加评论
     *
     * @param commentDto 评论dto
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> addComment(CommentDto commentDto) {
        // 评论有两种情况，一种是回复，一种直接评论
        ArticleComment articleComment = new ArticleComment();
        articleComment.setContent(commentDto.getContent()).setUserId(commentDto.getUserId())
                .setArticleId(commentDto.getArticleId());
        //parentId不为空就是回复
        if(commentDto.getParentId()!=null && !DataUtils.isEmpty(commentDto.getParentId().toString())){
            ArticleComment parentComment = articleCommentMapper.selectById(commentDto.getParentId());
            if(parentComment==null){
                return Result.error(ResponseError.COMMENT_ERROR);
            }
            articleComment.setParentId(parentComment.getId());
            //设置finalId
            Long finalId = parentComment.getFinalId() == null ? parentComment.getId() : parentComment.getFinalId();
            articleComment.setFinalId(finalId);
        }
        articleCommentMapper.insert(articleComment);
        return Result.success();
    }
}
