package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.textservice.entity.ArticleComment;
import com.llpy.textservice.entity.BulletChat;
import com.llpy.textservice.entity.UserComment;
import com.llpy.textservice.entity.dto.CommentDto;
import com.llpy.textservice.entity.vo.ArticleCommonVo;
import com.llpy.textservice.entity.vo.BulletChatVo;
import com.llpy.textservice.feign.UserService;
import com.llpy.textservice.feign.entity.UserDto2;
import com.llpy.textservice.feign.entity.UserVo;
import com.llpy.textservice.mapper.ArticleCommentMapper;
import com.llpy.textservice.mapper.ArticleMapper;
import com.llpy.textservice.mapper.BulletChatMapper;
import com.llpy.textservice.mapper.UserCommentMapper;
import com.llpy.textservice.service.CommentService;
import com.llpy.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ThreadPoolTaskExecutor anotherTaskExecutor;

    private final BulletChatMapper bulletChatMapper;

    public CommentServiceImpl(ArticleCommentMapper articleCommentMapper, ArticleMapper articleMapper, UserService userService, UserCommentMapper userCommentMapper, @Qualifier("anotherTaskExecutor") ThreadPoolTaskExecutor anotherTaskExecutor, BulletChatMapper bulletChatMapper) {
        this.articleCommentMapper = articleCommentMapper;
        this.articleMapper = articleMapper;
        this.userService = userService;
        this.userCommentMapper = userCommentMapper;
        this.anotherTaskExecutor = anotherTaskExecutor;
        this.bulletChatMapper = bulletChatMapper;
    }

    @Override
    public Result<?> listComment(Long articleId, Integer pageSize, Integer pageNum,Long userId) {
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

        //获取该用户点赞过这篇文章的评论
        Map<Long, Boolean> commentLikes = userCommentMapper.listByUserIdAndArticleId(userId, articleId).stream()
                .collect(Collectors.toMap(commentId -> commentId, commentId -> true));

        //构造评论视图对象
        for (ArticleCommonVo record : records) {
            //将父级存入集合中
            userIdByCommentIdMap.put(record.getId(), record.getUserId());
            //设置父级评论的信息
            record.setUserImg(userData.get(record.getUserId()).getUserImg());
            record.setNickname(userData.get(record.getUserId()).getNickname());
            // 设置是否点赞
            record.setShowDelete(false).setLiked(commentLikes.getOrDefault(record.getId(), false));

            //设置子级评论
            List<ArticleCommonVo> subComment = new ArrayList<>();
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
                articleCommonVo.setNickname(userData.get(articleComment.getUserId()).getNickname());
                articleCommonVo.setUserImg(userData.get(articleComment.getUserId()).getUserImg());

                Long parentUserId = userIdByCommentIdMap.get(articleComment.getParentId());
                String replyUserName = parentUserId == null ? "该评论已删除" : userData.get(parentUserId).getNickname();
                articleCommonVo.setReplyNickname(replyUserName);
                articleCommonVo.setShowDelete(false).setLiked(commentLikes.getOrDefault(articleComment.getId(), false));
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
    @Transactional
    public Result<?> deleteComment(Long commentId) {
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        if (articleComment == null) {
            return Result.error(ResponseError.COMMON_ERROR);
        }
        //评论-1然后删除
        articleMapper.commentCountReduceOne(articleComment.getArticleId());
        int i = articleCommentMapper.deleteById(commentId);

        //删除所有点赞和所有子级
        if (i > 0) {
            //找出该评论的所有子级
            //如果评论是一级，直接删除所有子级评论
            Long finalId = articleComment.getFinalId();
            if (finalId == null) {
                //异步删除,因为一级评论删除，所有子级不可见，可以采用异步
                anotherTaskExecutor.submit(() -> deleteByParentId(commentId));
                return Result.success();
            }
            //非一级评论，删除所有子级
            articleCommentMapper.deleteCommentByParentId(commentId);
            userCommentMapper.deleteByParentId(commentId);
        }
        Long articleId = articleComment.getArticleId();
        Integer sum = articleCommentMapper.countByArticleId(articleId);
        articleMapper.updateCommentCount(articleId, sum);
        return Result.success();
    }

    /**
     * 按最终id删除点赞信息，异步
     *
     * @param finalId 最终id
     */
    public void deleteByParentId(Long finalId) {
        //删除点赞信息
        userCommentMapper.deleteByParentId(finalId);
        List<Long> ids = articleCommentMapper.selectSubComment(DataUtils.longToList(finalId))
                .stream().map(ArticleComment::getId).collect(Collectors.toList());
        for (Long id : ids) {
            userCommentMapper.deleteByParentId(id);
        }
        //删除所有最终id为finalId的子级
        articleCommentMapper.deleteCommentByFinalId(finalId);
    }

    @Override
    public Result<?> likeComment(Long commentId, Long userId) {
        //获取评论
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        if(articleComment==null){
            return Result.error(ResponseError.COMMENT_ERROR);
        }
        //取消点赞
        UserComment oneByUserAndComment = userCommentMapper.getOneByUserAndComment(commentId, userId);
        if (oneByUserAndComment != null) {
            //删除
            userCommentMapper.deleteById(oneByUserAndComment);
            articleComment.setLikeSum(articleComment.getLikeSum() - 1);
        }else{
            //点赞
            UserComment userComment = new UserComment();
            Long parentId = articleComment.getParentId() == null ? articleComment.getId() : articleComment.getParentId();
            userComment.setCommentId(commentId).setUserId(userId).setCommentParent(parentId).setArticleId(articleComment.getArticleId());
            //插入
            userCommentMapper.insert(userComment);
            articleComment.setLikeSum(articleComment.getLikeSum() + 1);
        }
        //更新点赞数
        articleCommentMapper.updateById(articleComment);
        return Result.success();
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        userCommentMapper.deleteByArticleId(articleId);
        articleCommentMapper.deleteByArticleId(articleId);
    }

    @Override
    public Result<?> addBulletChat(BulletChat bulletChat, Long userId) {
        if (bulletChat.getText() == null || bulletChat.getText().isEmpty()) {
            return Result.error(ResponseError.COMMENT_ERROR);
        }
        //远程调用获取用户信息
        UserDto2 user = userService.getUser(userId);
        if (user == null) {
            return Result.error(ResponseError.COMMON_ERROR);
        }
        bulletChat.setUserId(userId).setText(bulletChat.getText()).setUserImg(user.getUserImg());
        bulletChatMapper.insert(bulletChat);
       return Result.success();
    }

    @Override
    public Result<?> getBulletChat() {
        List<BulletChatVo> bulletChatVos = bulletChatMapper.getBulletChat();
        return Result.success(bulletChatVos);
    }
}
