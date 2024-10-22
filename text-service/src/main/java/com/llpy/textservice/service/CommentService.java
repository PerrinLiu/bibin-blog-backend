package com.llpy.textservice.service;


import com.llpy.model.Result;
import com.llpy.textservice.entity.BulletChat;
import com.llpy.textservice.entity.dto.CommentDto;

/**
 * 评论服务
 *
 * @author llpy
 * @date 2024/06/26
 */
public interface CommentService {
    /**
     * 获取评论
     *
     * @param articleId 文章id
     * @param pageSize  页面大小
     * @param pageNum   书籍页码
     * @return {@code Result<?>}
     */
    Result<?> listComment(Long articleId, Integer pageSize, Integer pageNum,Long userId);

    /**
     * 添加评论
     * commentDto的parentId为空就是为一级评论
     *
     * @param commentDto 评论dto
     * @return {@code Result<?>}
     */
    Result<?> addComment(CommentDto commentDto);

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @return {@code Result<?>}
     */
    Result<?> deleteComment(Long commentId);

    /**
     * 点赞或取消点赞评论
     *
     * @param commentId 评论id
     * @param userId    用户id
     * @return {@code Result<?>}
     */
    Result<?> likeComment(Long commentId, Long userId);

    /**
     * 按文章id删除
     *
     * @param articleId 文章id
     */
    void deleteByArticleId(Long articleId);

    /**
     * 添加子弹聊天
     *
     * @param bulletChat 子弹聊天
     * @param userId     用户id
     * @return {@code Result<?>}
     */
    Result<?> addBulletChat(BulletChat bulletChat,Long userId);

    /**
     * 获取子弹聊天
     *
     * @return {@code Result<?>}
     */
    Result<?> getBulletChat();

    /**
     * 获取最近评论
     *
     * @return {@code Result<?>}
     */
    Result<?> getRecentComment();

}
