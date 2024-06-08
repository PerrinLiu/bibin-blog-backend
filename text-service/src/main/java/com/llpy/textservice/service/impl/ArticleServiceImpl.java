package com.llpy.textservice.service.impl;

import com.llpy.model.CodeMsg;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Article;
import com.llpy.textservice.entity.ArticleText;
import com.llpy.textservice.entity.dto.ArticleDto;
import com.llpy.textservice.mapper.ArticleMapper;
import com.llpy.textservice.mapper.ArticleTextMapper;
import com.llpy.textservice.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llpy.utils.AliOSSUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    public ArticleServiceImpl(AliOSSUtils aliOSSUtils, ArticleMapper articleMapper, ArticleTextMapper articleTextMapper) {
        this.aliOSSUtils = aliOSSUtils;
        this.articleMapper = articleMapper;
        this.articleTextMapper = articleTextMapper;
    }

    @Override
    public Result<?> uploadImg(MultipartFile file) {
        try {
            String upload = aliOSSUtils.upload(file);
            return Result.success(upload);
        } catch (IOException e) {
            return new Result<>(CodeMsg.UPLOAD_IMG_ERROR);
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
        article.setArticleTitle(articleDto.getTitle()).setArticleGroupId(articleDto.getGroupId()).setCreatBy(userId);

        //添加文章内容后获取内容id，设置给文章
        ArticleText articleText = new ArticleText();
        articleText.setArticleText(articleDto.getArticleText());
        articleTextMapper.insert(articleText);
        article.setArticleTextId(articleText.getId());

        //添加文章
        articleMapper.insert(article);

        return Result.success();
    }
}
