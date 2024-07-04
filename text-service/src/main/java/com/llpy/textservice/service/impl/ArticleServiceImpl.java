package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.textservice.entity.*;
import com.llpy.textservice.entity.dto.ArticleDto;
import com.llpy.textservice.entity.vo.ArticleDetailsVo;
import com.llpy.textservice.entity.vo.ArticleCountVo;
import com.llpy.textservice.mapper.*;
import com.llpy.textservice.service.ArticleService;
import com.llpy.textservice.service.CommentService;
import com.llpy.utils.AliOSSUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
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

    private final CommentService commentService;

    private final ThreadPoolTaskExecutor taskExecutor;

    private final DiaryMapper diaryMapper;

    public ArticleServiceImpl(AliOSSUtils aliOSSUtils, ArticleMapper articleMapper, ArticleTextMapper articleTextMapper, ArticleGroupMapper articleGroupMapper, UserArticleMapper userArticleMapper, CommentService commentService, @Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor, DiaryMapper diaryMapper) {
        this.aliOSSUtils = aliOSSUtils;
        this.articleMapper = articleMapper;
        this.articleTextMapper = articleTextMapper;
        this.articleGroupMapper = articleGroupMapper;
        this.userArticleMapper = userArticleMapper;
        this.commentService = commentService;
        this.taskExecutor = taskExecutor;
        this.diaryMapper = diaryMapper;
    }

    /**
     * 上传图片到ali oss
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
        //添加文章,如果文章id不为空，代表是更新
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
        //设置文章内容
        ArticleText articleText = new ArticleText();
        articleText.setArticleText(articleDto.getArticleText());
        //如果文章id不为空，代表是更新
        if (articleDto.getArticleId() != null) {
            article.setId(articleDto.getArticleId());
            articleMapper.updateById(article);
            //根据文章id获取文章内容id
            Long articleTextId = articleTextMapper.getOneByArticleId(article.getId());
            articleText.setId(articleTextId);
            articleTextMapper.updateById(articleText);
        } else {
            //插入文章内容，获取内容id，设置给文章
            articleTextMapper.insert(articleText);
            article.setArticleTextId(articleText.getId());
            //添加文章
            articleMapper.insert(article);
        }
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
        //更新文章点赞和收藏数
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

    @Override
    public Result<?> deleteArticle(Long articleId, Long userId) {
        //判断是否为作者
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return Result.error(ResponseError.COMMON_ERROR);
        }
        //删除文章
        articleMapper.deleteById(articleId);
        // TODO: 2024/7/3 待删除文章内容数据 
        taskExecutor.submit(() -> {
            System.out.println("删除文章后，异步删除评论和评论的点赞信息");
            //删除文章的点赞信息
            userArticleMapper.deleteByArticleId(articleId);
            //删除文章评论和评论的点赞信息
            commentService.deleteByArticleId(articleId);
            System.out.println("异步结束");
        });
        System.out.println("退出");
        return Result.success();
    }

    @Override
    public HashMap<String, Integer> getCountText() {
        HashMap<String, Integer> res = new HashMap<>();
        //根据时间获取每天的文章数
        List<ArticleCountVo> list = articleMapper.selectCountByDate();
        for (ArticleCountVo articleCountVo : list) {
            res.put(articleCountVo.getDate(),articleCountVo.getCount());
        }
        //获取总文章数和组数
        int articleCount = articleMapper.selectCount(null);
        int groupCount = articleGroupMapper.selectCount(null);
        LambdaQueryWrapper<Diary> query = new LambdaQueryWrapper<>();
        query.eq(Diary::getDiaryStatus,2);
        int diaryCount = diaryMapper.selectCount(query);
        res.put("articleCount",articleCount);
        res.put("groupCount",groupCount);
        res.put("diaryCount",diaryCount);
        return res;
    }

    @Override
    public Result<?> addGroup(String groupName) {
        int i = articleGroupMapper.selectByName(groupName);
        if (i !=0) {
            return Result.error("该分组已经存在");
        }
        ArticleGroup articleGroup = new ArticleGroup();
        articleGroup.setArticleType(groupName);
        articleGroupMapper.insert(articleGroup);
        return Result.success();
    }


}
