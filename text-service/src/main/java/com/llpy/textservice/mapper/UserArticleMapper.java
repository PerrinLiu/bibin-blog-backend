package com.llpy.textservice.mapper;

import com.llpy.textservice.entity.UserArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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

    UserArticle getOneByUserIdAndArticleId(Long userId, Long articleId);
}
