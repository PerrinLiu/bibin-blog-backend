package com.llpy.textservice.mapper;

import com.llpy.textservice.entity.UserComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LLPY
 * @since 2024-07-02
 */
@Mapper
public interface UserCommentMapper extends BaseMapper<UserComment> {

    /**
     * 按用户id和评论id获取一个
     *
     * @param commentId 评论id
     * @param userId    用户id
     * @return {@code UserComment}
     */
    UserComment getOneByUserAndComment(Long commentId, Long userId);

    /**
     * 按父id删除点赞
     *
     * @param parentId 父id
     */
    void deleteByParentId(Long parentId);

    /**
     * 获取指定用户点赞过的指定文章的评论
     *
     * @param userId    用户id
     * @param articleId 文章id
     * @return {@code List<Long>}
     */
    List<Long> listByUserIdAndArticleId(Long userId, Long articleId);

}
