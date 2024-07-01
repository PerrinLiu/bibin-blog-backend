package com.llpy.textservice.controller;


import com.llpy.annotation.OperateLog;
import com.llpy.model.Result;
import com.llpy.textservice.entity.dto.CommentDto;
import com.llpy.textservice.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 评论控制器
 *
 * @author llpy
 * @date 2024/06/26
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论控制类")
public class CommentController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/common/listComment")
    @ApiOperation("获取评论列表")
    public Result<?> listComment(@RequestParam Long articleId,@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        return commentService.listComment(articleId,pageSize,pageNum);
    }

    @PostMapping("/addComment")
    @ApiOperation("添加评论或回复")
    @OperateLog("添加评论或回复")
    public Result<?> addComment(@RequestBody CommentDto commentDto) {
        return commentService.addComment(commentDto);
    }

    @DeleteMapping("/deleteComment")
    @ApiOperation("删除评论")
    public Result<?> deleteComment(@RequestParam Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @PostMapping("/likeComment")
    @ApiOperation("点赞评论")
    public Result<?> likeComment(@RequestParam Long commentId,@RequestParam Long userId) {
        return commentService.likeComment(commentId,userId);
    }
}
