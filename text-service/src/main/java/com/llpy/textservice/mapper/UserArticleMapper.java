package com.llpy.textservice.mapper;

import com.llpy.textservice.entity.UserArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llpy.textservice.entity.vo.UserArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户对文章进行的操作记录表，点赞和收藏 Mapper 接口
 * </p>
 *
 * @author LLPY
 * @since 2024-06-23
 */
@Mapper
public interface UserArticleMapper extends BaseMapper<UserArticle> {

    /**
     * 按用户id和文章id获取一个点赞信息
     *
     * @param userId    用户id
     * @param articleId 文章id
     * @return {@code UserArticle}
     */
    UserArticle getOneByUserIdAndArticleId(Long userId, Long articleId);

    /**
     * 按项目id删除点赞信息
     *
     * @param articleId 文章id
     */
    void deleteByArticleId(Long articleId);

    /**
     * 获取用户最近的10条点赞记录
     *
     * @param userId 用户id
     * @return {@code List<UserArticle>}
     */
    List<UserArticleVo> lastArticle(Long userId);
}
