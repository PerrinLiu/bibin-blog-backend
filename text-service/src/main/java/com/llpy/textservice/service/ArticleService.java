package com.llpy.textservice.service;

import com.llpy.model.Result;
import com.llpy.textservice.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.llpy.textservice.entity.dto.ArticleDto;
import io.swagger.models.auth.In;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author HXB
 * @since 2024-06-08
 */
public interface ArticleService extends IService<Article> {

    /**
     * 上传img
     *
     * @param file 文件
     * @return {@code Result<?>}
     */
    Result<?> uploadImg(MultipartFile file);

    /**
     * 添加文章
     *
     * @param articleDto 文章dto
     * @param userId     用户id
     * @return {@code Result<?>}
     */
    Result<?> addArticle(ArticleDto articleDto, Long userId);

    /**
     * 获取组列表
     *
     * @return {@code Result<?>}
     */
    Result<?> getGroupList();

    /**
     * 列出文章
     *
     * @param pageSize   页面大小
     * @param pageNum    书籍页码
     * @param searchText 搜索文本
     * @param userId     用户id
     * @return {@code Result<?>}
     */
    Result<?> listArticle(Integer pageSize, Integer pageNum, String searchText, Long userId);

    /**
     * 根据id获取文章
     *
     * @param articleId 文章id
     * @param userId    用户id
     * @return {@code Result<?>}
     */
    Result<?> getArticle(Long articleId, Long userId);


    /**
     * 喜欢或点赞文章
     *
     * @param articleId 文章id
     * @param userId    用户id
     * @param type      类型
     * @return {@code Result<?>}
     */
    Result<?> likeOrStarArticle(Long articleId, Long userId, Integer type);

    /**
     * 列出首页文章
     *
     * @return {@code Result<?>}
     */
    Result<?> listIndexArticle();


    /**
     * 删除文章
     *
     * @param articleId 文章id
     * @param userId    用户id
     * @return {@code Result<?>}
     */
    Result<?> deleteArticle(Long articleId, Long userId);

    /**
     * 获取计数文本,
     * String 为key Integer 为数
     * 有对应日期的文章输，以及articleCount,groupCount的数量
     * @return {@code Result<?>}
     */
    HashMap<String, Integer> getCountText();

    Result<?> addGroup(String groupName);
}
