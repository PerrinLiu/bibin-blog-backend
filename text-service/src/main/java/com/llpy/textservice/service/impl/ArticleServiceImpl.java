package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Article;
import com.llpy.textservice.entity.ArticleGroup;
import com.llpy.textservice.entity.ArticleText;
import com.llpy.textservice.entity.UserArticle;
import com.llpy.textservice.entity.dto.ArticleDto;
import com.llpy.textservice.entity.vo.ArticleDetailsVo;
import com.llpy.textservice.mapper.ArticleGroupMapper;
import com.llpy.textservice.mapper.ArticleMapper;
import com.llpy.textservice.mapper.ArticleTextMapper;
import com.llpy.textservice.mapper.UserArticleMapper;
import com.llpy.textservice.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llpy.utils.AliOSSUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author HXB
 * @since 2024-06-08
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {


    private final AliOSSUtils aliOSSUtils;

    private final ArticleMapper articleMapper;

    private final ArticleTextMapper articleTextMapper;

    private final ArticleGroupMapper articleGroupMapper;

    private final UserArticleMapper userArticleMapper;

    public ArticleServiceImpl(AliOSSUtils aliOSSUtils, ArticleMapper articleMapper, ArticleTextMapper articleTextMapper, ArticleGroupMapper articleGroupMapper, UserArticleMapper userArticleMapper) {
        this.aliOSSUtils = aliOSSUtils;
        this.articleMapper = articleMapper;
        this.articleTextMapper = articleTextMapper;
        this.articleGroupMapper = articleGroupMapper;
        this.userArticleMapper = userArticleMapper;
    }

    /**
     * 上传img
     *
     * @param file 文件
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> uploadImg(MultipartFile file) {
        try {
            String upload = aliOSSUtils.upload(file);
            return Result.success(upload);
        } catch (IOException e) {
            return Result.error(ResponseError.UPLOAD_IMG_ERROR);
        }
    }

    /**
     * 添加文章
     *
     * @param articleDto 文章dto
     * @param userId     用户id
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> addArticle(ArticleDto articleDto, Long userId) {
        Article article = new Article();
        article.setArticleTitle(articleDto.getTitle()).setCreatBy(userId);
        StringBuilder groupId = new StringBuilder();
        for (Integer i : articleDto.getGroupIds()) {
            if (groupId.length() > 0) {
                groupId.append(",");
            }
            groupId.append(i);
        }
        article.setArticleGroupId(groupId.toString()).setCover(articleDto.getCover()).setDes(articleDto.getDes());

        //添加文章内容后获取内容id，设置给文章
        ArticleText articleText = new ArticleText();
        articleText.setArticleText(articleDto.getArticleText());
        articleTextMapper.insert(articleText);
        article.setArticleTextId(articleText.getId());

        //添加文章
        articleMapper.insert(article);

        return Result.success();
    }

    /**
     * 获取组列表
     *
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> getGroupList() {
        List<ArticleGroup> articleTexts = articleGroupMapper.selectList(null);
        return Result.success(articleTexts);
    }

    /**
     * 列出文章
     *
     * @param pageSize   页面大小
     * @param pageNum    书籍页码
     * @param searchText 搜索文本
     * @param userId     用户id
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> listArticle(Integer pageSize, Integer pageNum, String searchText, Long userId) {
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        //todo 后续根据用户点赞，收藏，评论数排序
        //得到基本信息
        articleMapper.getArticleList(articlePage, searchText);
        return Result.success(articlePage);
    }

    /**
     * 获取文章
     *
     * @param articleId 文章id
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> getArticle(Long articleId, Long userId) {
        // TODO: 2024/6/23 根据用户id判断是否已经点过赞和收藏
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return Result.error(ResponseError.NOT_FOUND_ERROR);
        }
        Integer readSum = article.getReadSum();
        article.setReadSum(readSum + 1);
        updateReadSum(article);

        //创建返回对象
        ArticleDetailsVo articleDetailsVo = new ArticleDetailsVo();
        if (userId != null) {
            //判断是否点赞和收藏
            UserArticle userArticle = userArticleMapper.getOneByUserIdAndArticleId(userId, articleId);
            if (userArticle != null) {
                articleDetailsVo.setLiked(userArticle.getLiked());
                articleDetailsVo.setStar(userArticle.getStar());
            }
        }
        //拷贝
        BeanUtils.copyProperties(article, articleDetailsVo);
        //根据文章id获取文章详情
        ArticleText articleText = articleTextMapper.selectById(article.getArticleTextId());
        articleDetailsVo.setArticleText(articleText.getArticleText());
        return Result.success(articleDetailsVo);
    }


    /**
     * 异步更新读取总和
     *
     * @param article 文章
     */
    @Async("taskExecutor")
    public void updateReadSum(Article article) {
        articleMapper.updateById(article);
    }

    /**
     * 获取文章评论
     *
     * @param articleId 文章id
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> getArticleComments(String articleId) {
        return null;
    }

    /**
     * 喜欢文章
     *
     * @param articleId 文章id
     * @param userId    用户id
     * @param type      类型 1点赞 2收藏
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> likeOrStarArticle(Long articleId, Long userId, Integer type) {
        if (userId == null) {
            return Result.error(ResponseError.USER_NOT_LOGIN);
        }

        //判断是否点赞和收藏
        UserArticle userArticle = userArticleMapper.getOneByUserIdAndArticleId(userId, articleId);
        //第一次的情况
        if (userArticle == null) {
            userArticle = new UserArticle();
            //设置点赞和收藏
            if (type == 1) {
                userArticle.setLiked(true);
            } else {
                userArticle.setStar(true);
            }
            userArticle.setUserId(userId).setArticleId(articleId);
            userArticleMapper.insert(userArticle);
        } else {
            if (type == 1) {
                userArticle.setLiked(!userArticle.getLiked());
            } else {
                userArticle.setStar(!userArticle.getStar());
            }
            userArticleMapper.updateById(userArticle);
        }
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return Result.error(ResponseError.NOT_FOUND_ERROR);
        }
        if (type == 1) {
            article.setLikeSum(userArticle.getLiked() ? 1 : -1);
        } else {
            article.setCollectionsSum(userArticle.getStar() ? 1 : -1);
        }
        articleMapper.updateById(article);
        return Result.success();
    }

    /**
     * 列出索引文章
     *
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> listIndexArticle() {
        //根据日期获取前5篇文章
        List<Article> articles = articleMapper.listIndexArticle();
        return Result.success(articles);
    }

}
