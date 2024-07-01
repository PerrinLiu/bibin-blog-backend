package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.textservice.entity.ArticleComment;
import com.llpy.textservice.entity.UserComment;
import com.llpy.textservice.entity.dto.CommentDto;
import com.llpy.textservice.entity.vo.ArticleCommonVo;
import com.llpy.textservice.feign.UserService;
import com.llpy.textservice.feign.entity.UserVo;
import com.llpy.textservice.mapper.ArticleCommentMapper;
import com.llpy.textservice.mapper.ArticleMapper;
import com.llpy.textservice.mapper.UserCommentMapper;
import com.llpy.textservice.service.CommentService;
import com.llpy.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论服务impl
 *
 * @author llpy
 * @date 2024/06/26
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final ArticleCommentMapper articleCommentMapper;

    private final ArticleMapper articleMapper;
    private final UserService userService;

    private final UserCommentMapper userCommentMapper;

    public CommentServiceImpl(ArticleCommentMapper articleCommentMapper, ArticleMapper articleMapper, UserService userService, UserCommentMapper userCommentMapper) {
        this.articleCommentMapper = articleCommentMapper;
        this.articleMapper = articleMapper;
        this.userService = userService;
        this.userCommentMapper = userCommentMapper;
    }

    @Override
    public Result<?> listComment(Long articleId, Integer pageSize, Integer pageNum) {
        //远程调用用户服务获取用户信息
        HashMap<Long, UserVo> userData = userService.getUserData();
        //构造分页，每次最多查询10条
        Page<ArticleCommonVo> articleCommentPage = new Page<>(pageNum, pageSize);
        IPage<ArticleCommonVo> articlePage = articleCommentMapper.listCommentFinal(articleId, articleCommentPage);
        List<ArticleCommonVo> records = articlePage.getRecords();
        //获取评论的子级
        List<Long> ids = records.stream().map(ArticleCommonVo::getId).collect(Collectors.toList());
        List<ArticleComment> articleComments = new ArrayList<>();
        if (!ids.isEmpty()) {
            articleComments = articleCommentMapper.selectSubComment(ids);
        }
        //根据最终父级划分子级
        Map<Long, List<ArticleComment>> commentByFinalIdMap = new HashMap<>();
        //评论id对应的用户id集合
        HashMap<Long, Long> userIdByCommentIdMap = new HashMap<>();
        for (ArticleComment articleComment : articleComments) {
            commentByFinalIdMap.putIfAbsent(articleComment.getFinalId(), new ArrayList<>());
            commentByFinalIdMap.get(articleComment.getFinalId()).add(articleComment);
            userIdByCommentIdMap.put(articleComment.getId(), articleComment.getUserId());
        }
        //构造评论视图对象
        for (ArticleCommonVo record : records) {
            //将父级存入集合中
            userIdByCommentIdMap.put(record.getId(), record.getUserId());
            //设置父级评论的信息
            record.setUserImg(userData.get(record.getUserId()).getUserImg());
            record.setUserName(userData.get(record.getUserId()).getUserName());
            // TODO: 2024/7/2 点赞标识待做
            record.setLiked(false).setShowDelete(false);
            List<ArticleCommonVo> subComment = new ArrayList<>();
            //设置子级评论
            record.setSubComment(subComment);
            //验证是否存在子级评论
            if (commentByFinalIdMap.get(record.getId()) == null || commentByFinalIdMap.get(record.getId()).isEmpty()) {
                continue;
            }
            //构造子级视图对象
            List<ArticleComment> subList = commentByFinalIdMap.get(record.getId());
            for (ArticleComment articleComment : subList) {
                //构造评论视图对象
                ArticleCommonVo articleCommonVo = new ArticleCommonVo();
                BeanUtils.copyProperties(articleComment, articleCommonVo);
                //设置用户名和图片以及回复的用户名
                articleCommonVo.setUserName(userData.get(articleComment.getUserId()).getUserName());
                articleCommonVo.setUserImg(userData.get(articleComment.getUserId()).getUserImg());

                Long parentUserId = userIdByCommentIdMap.get(articleComment.getParentId());
                String replyUserName = parentUserId == null ? "该评论已被删除" : userData.get(parentUserId).getUserName();
                articleCommonVo.setReplyUserName(replyUserName);
                articleCommonVo.setLiked(false).setShowDelete(false);
                subComment.add(articleCommonVo);
            }
        }
        return Result.success(articlePage);
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
        if (commentDto.getParentId() != null && !DataUtils.isEmpty(commentDto.getParentId().toString())) {
            ArticleComment parentComment = articleCommentMapper.selectById(commentDto.getParentId());
            if (parentComment == null) {
                return Result.error(ResponseError.COMMENT_ERROR);
            }
            articleComment.setParentId(parentComment.getId());
            //设置finalId
            Long finalId = parentComment.getFinalId() == null ? parentComment.getId() : parentComment.getFinalId();
            articleComment.setFinalId(finalId);
        }
        articleCommentMapper.insert(articleComment);
        //评论数加一
        Long articleId = articleComment.getArticleId();
        articleMapper.commentCountAddOne(articleId);
        return Result.success();
    }

    @Override
    public Result<?> deleteComment(Long commentId) {
        // TODO: 2024/7/2 需要将评论数-1
        articleCommentMapper.deleteById(commentId);
        return Result.success();
    }

    @Override
    public Result<?> likeComment(Long commentId, Long userId) {
        // TODO: 2024/7/2 待完善 ，点赞数+1
        UserComment userComment = new UserComment();
        userComment.setCommentId(commentId).setUserId(userId);
        userCommentMapper.insert(userComment);
        return Result.success();
    }
}
