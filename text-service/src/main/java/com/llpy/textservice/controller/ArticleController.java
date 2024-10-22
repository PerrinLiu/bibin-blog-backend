package com.llpy.textservice.controller;


import com.llpy.annotation.OperateLog;
import com.llpy.controller.BaseController;
import com.llpy.model.Result;
import com.llpy.textservice.entity.dto.ArticleDto;
import com.llpy.textservice.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

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
@Api(tags = "文章控制类")
public class ArticleController extends BaseController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/uploadImg")
    @ApiOperation(value = "上传图片")
    @OperateLog("上传图片到阿里云")
    public Result<?> updateImg(@RequestParam("image") MultipartFile image) {
        return articleService.uploadImg(image);
    }


    @PostMapping("/addArticle")
    @ApiOperation(value = "添加文章")
    @OperateLog("添加文章")
    public Result<?> addArticle(@Valid @RequestBody ArticleDto articleDto) {
        return articleService.addArticle(articleDto, loginUser().getUserId());
    }

    @DeleteMapping("/deleteArticle")
    @ApiOperation(value = "删除文章")
    @OperateLog("删除文章")
    public Result<?> deleteArticle(@NotNull(message = "文章id不能为空") @NotBlank(message = "文章id不能为空") @RequestParam Long articleId) {
        return articleService.deleteArticle(articleId, loginUser().getUserId());
    }

    @GetMapping("/listArticle")
    @ApiOperation(value = "获得文章列表")
    public Result<?> listArticle(Integer pageSize, Integer pageNum,String sort) {
        return articleService.listArticle(pageSize, pageNum, sort);
    }

    @GetMapping("/common/searchArticle")
    @ApiOperation(value = "搜索文章列表")
    public Result<?> searchArticle(Integer pageSize, Integer pageNum, String searchText,String groups,String sort) {
        return articleService.searchArticle(pageSize, pageNum, searchText, groups,sort);
    }

    @GetMapping("/common/recommendArticle")
    @ApiOperation(value = "根据用户点赞获得推荐文章")
    public Result<?> recommendArticle() {
        return articleService.recommendArticle(loginUser().getUserId());
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


    @PostMapping("/addGroup")
    @ApiOperation(value = "添加文章分组")
    @OperateLog("添加文章分组")
    public Result<?> addGroup(@RequestParam String groupName) {
        return articleService.addGroup(groupName);
    }



    @GetMapping("common/getCountText")
    @ApiOperation(value = "获取统计数据")
    public HashMap<String, Integer> getCountText() {
        return articleService.getCountText();
    }
}

