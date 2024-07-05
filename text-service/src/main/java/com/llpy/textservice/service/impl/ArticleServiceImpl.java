package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.textservice.entity.*;
import com.llpy.textservice.entity.dto.ArticleDto;
import com.llpy.textservice.entity.vo.*;
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
import java.util.*;

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
     * 获取组列表和组的文章数量
     *
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> getGroupList() {
        //获取分组数据
        List<ArticleGroup> articleGroups = articleGroupMapper.selectList(null);
        List<ArticleGroupVo> res = new ArrayList<>();
        //初始化记录每个分组文章数量的map
        HashMap<Long, Integer> groupNumber = new HashMap<>();
        for (ArticleGroup articleGroup : articleGroups) {
            ArticleGroupVo articleGroupVo = new ArticleGroupVo();
            BeanUtils.copyProperties(articleGroup, articleGroupVo);
            res.add(articleGroupVo);
            groupNumber.put(articleGroup.getId(), 0);
        }
        //获取每个分组的个数
        List<ArticleGroupVo> articleGroupVos = articleMapper.selectGroupAndCount();
        for (ArticleGroupVo articleGroupVo : articleGroupVos) {
            //有可能组合，拆分成数组。
            String articleType = articleGroupVo.getArticleType();
            String[] split = articleType.split(",");
            for (String s : split) {
                Long id = Long.valueOf(s);
                groupNumber.put(id, groupNumber.get(id) + articleGroupVo.getNumber());
            }
        }
        //将分组数量设置给返回结果
        groupNumber.forEach((k, v) -> {
            for (ArticleGroupVo articleGroupVo : res) {
                if (k.equals(articleGroupVo.getId())) {
                    articleGroupVo.setNumber(v);
                }
            }
        });
        return Result.success(res);
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
            Integer likeSum = article.getLikeSum();
            article.setLikeSum(likeSum + (userArticle.getLiked() ? 1 : -1));
        } else {
            Integer collectionsSum = article.getCollectionsSum();
            article.setCollectionsSum(collectionsSum + (userArticle.getStar() ? 1 : -1));
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
            res.put(articleCountVo.getDate(), articleCountVo.getCount());
        }
        //获取总文章数和组数
        int articleCount = articleMapper.selectCount(null);
        int groupCount = articleGroupMapper.selectCount(null);
        LambdaQueryWrapper<Diary> query = new LambdaQueryWrapper<>();
        query.eq(Diary::getDiaryStatus, 2);
        int diaryCount = diaryMapper.selectCount(query);
        res.put("articleCount", articleCount);
        res.put("groupCount", groupCount);
        res.put("diaryCount", diaryCount);
        return res;
    }

    @Override
    public Result<?> addGroup(String groupName) {
        int i = articleGroupMapper.selectByName(groupName);
        if (i != 0) {
            return Result.error("该分组已经存在");
        }
        ArticleGroup articleGroup = new ArticleGroup();
        articleGroup.setArticleType(groupName);
        articleGroupMapper.insert(articleGroup);
        return Result.success();
    }

    /**
     * 推荐文章
     *
     * @param userId 用户id
     * @return {@code Result<?>}
     */
    @Override
    public Result<?> recommendArticle(Long userId) {
        //如果用户没登陆，则返回最近的五个
        if (userId == null) {
            List<RecommendArticleVo> recommendArticleVo = articleMapper.recommendArticle(null, 5);
            return Result.success(recommendArticleVo);
        }
        //最近的10条点赞和收藏的文章分组信息
        List<UserArticleVo> articles = userArticleMapper.lastArticle(userId);
        //统计每个分组的权重，点赞和收藏的权重1：1
        HashMap<String, Integer> articleMap = getWeightByGroup(articles);
        //构建返回数据
        List<RecommendArticleVo> res = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : articleMap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            //根据分组和个数，找出文章
            List<RecommendArticleVo> recommendArticleVo = articleMapper.recommendArticle(key, value);
            //不同的文章添加到返回数据
            addRes(res, recommendArticleVo);
        }
        int diff = 5 - res.size();
        //如果文章不够5个，获取最近的五篇，补足5篇文章
        if(diff > 0){
            List<RecommendArticleVo> recommendArticleVo = articleMapper.recommendArticle(null, 5);
            addRes(res, recommendArticleVo);
        }
        return Result.success(res);
    }

    /**
     * 按组计算体重
     *
     * @param articles 见习契约
     * @return {@code HashMap<String, Integer>}
     */
    private static HashMap<String, Integer> getWeightByGroup(List<UserArticleVo> articles) {
        HashMap<String, Integer> map = new HashMap<>();
        //总权重，（总权重/5,就是一篇文章需要的权重，例如总权重为5，则代表分组权重大于1的才需要找文章）
        int weightSum = 0;
        for (UserArticleVo article : articles) {
            String[] split = article.getGroupList().split(",");
            //分组权重
            int weight = article.getLiked() + article.getStar();
            weightSum += weight;
            for (String s : split) {
                map.putIfAbsent(s, 0);
                map.put(s, map.get(s) + weight);
            }
        }
        //每个文章需要的权重
        int weight = weightSum / 5;
        //分组需要找出的文章
        HashMap<String, Integer> articleMap = new HashMap<>();
        for (Map.Entry<String, Integer> mapEntry : map.entrySet()) {
            if (mapEntry.getValue() >= weight) {
                articleMap.put(mapEntry.getKey(), mapEntry.getValue() / weight);
            }
        }
        return articleMap;
    }

    /**
     * 将不同的文章添加res
     *
     * @param res                物件
     * @param recommendArticleVo 推荐文章vo
     */
    private void addRes(List<RecommendArticleVo> res, List<RecommendArticleVo> recommendArticleVo) {
        for (RecommendArticleVo articleVo : recommendArticleVo) {
            boolean flag = true;
            for (RecommendArticleVo recommendArticleVo2 : res) {
                if (articleVo.getId().equals(recommendArticleVo2.getId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                res.add(articleVo);
            }
        }
    }


}
