package com.llpy.textservice.service;


import com.llpy.model.Result;
import com.llpy.textservice.entity.dto.CommentDto;

/**
 * 评论服务
 *
 * @author llpy
 * @date 2024/06/26
 */
public interface CommentService {
    Result<?> listComment(Long articleId, Integer pageSize, Integer pageNum);

    Result<?> addComment(CommentDto commentDto);

    Result<?> deleteComment(Long commentId);

    Result<?> likeComment(Long commentId, Long userId);
}
