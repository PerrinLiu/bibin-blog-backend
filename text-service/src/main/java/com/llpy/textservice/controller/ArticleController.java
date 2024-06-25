package com.llpy.textservice.controller;


import com.llpy.controller.BaseController;
import com.llpy.model.Result;
import com.llpy.textservice.entity.dto.ArticleDto;
import com.llpy.textservice.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author HXB
 * @since 2024-06-08
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/uploadImg")
    @ApiOperation(value = "上传图片")
    public Result<?> updateImg(@RequestParam("image") MultipartFile image) {
        return articleService.uploadImg(image);
    }

    @PostMapping("/common/uploadFile")
    @ApiOperation(value = "上传文件")
    public Result<?> updateFile(@RequestParam("file") MultipartFile file) {
        return articleService.uploadFile(file);
    }

    @PostMapping("/addArticle")
    @ApiOperation(value = "添加文章")
    public Result<?> addArticle(@Valid @RequestBody ArticleDto articleDto) {
        return articleService.addArticle(articleDto, loginUser().getUserId());
    }

    @GetMapping("/listArticle")
    @ApiOperation(value = "获得文章列表")
    public Result<?> listArticle(Integer pageSize, Integer pageNum, String searchText) {
        return articleService.listArticle(pageSize, pageNum, searchText, loginUser().getUserId());
    }

    @GetMapping("/common/listIndexArticle")
    @ApiOperation(value = "获得首页文章列表")
    public Result<?> listIndexArticle() {
        return articleService.listIndexArticle();
    }

    @GetMapping("/common/getArticle")
    @ApiOperation(value = "获得文章")
    public Result<?> getArticle(@NotNull(message = "文章id不能为空") @NotBlank(message = "文章id不能为空") @RequestParam Long articleId) {
        return articleService.getArticle(articleId,loginUser().getUserId());
    }

    @GetMapping("/getArticleComments")
    @ApiOperation(value = "获得文章评论")
    public Result<?> getArticleDetails(@NotNull(message = "文章id不能为空") @NotBlank(message = "文章id不能为空") @RequestParam String articleId) {
        return articleService.getArticleComments(articleId);
    }

    @PostMapping("/likeOrStarArticle")
    @ApiOperation(value = "点赞或收藏文章")
    public Result<?> likeArticle(@NotNull(message = "文章id不能为空") @NotBlank(message = "文章id不能为空") @RequestParam Long articleId, @RequestParam Integer type) {
        return articleService.likeOrStarArticle(articleId, loginUser().getUserId(), type);
    }

    @GetMapping("/getGroupList")
    @ApiOperation(value = "获得文章分组")
    public Result<?> getGroupList() {
        return articleService.getGroupList();
    }
}

