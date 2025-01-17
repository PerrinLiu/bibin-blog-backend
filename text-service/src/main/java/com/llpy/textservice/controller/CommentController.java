package com.llpy.textservice.controller;


import com.llpy.annotation.OperateLog;
import com.llpy.controller.BaseController;
import com.llpy.model.Result;
import com.llpy.textservice.entity.BulletChat;
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
public class CommentController extends BaseController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/common/listComment")
    @ApiOperation("获取评论列表")
    public Result<?> listComment(@RequestParam Long articleId,@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        return commentService.listComment(articleId,pageSize,pageNum,loginUser().getUserId());
    }

    @PostMapping("/addComment")
    @ApiOperation("添加评论或回复")
    @OperateLog("添加评论或回复")
    public Result<?> addComment(@RequestBody CommentDto commentDto) {
        return commentService.addComment(commentDto);
    }

    @DeleteMapping("/deleteComment")
    @ApiOperation("删除评论")
    @OperateLog("删除评论")
    public Result<?> deleteComment(@RequestParam Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @PostMapping("/likeComment")
    @ApiOperation("点赞评论")
    public Result<?> likeComment(@RequestParam Long commentId,@RequestParam Long userId) {
        return commentService.likeComment(commentId,userId);
    }

    @PostMapping("/addDanMu")
    @ApiOperation("添加弹幕")
    @OperateLog("添加弹幕")
    public Result<?> addBulletChat(@RequestBody BulletChat bulletChat) {
        return commentService.addBulletChat(bulletChat, loginUser().getUserId());
    }

    @GetMapping("/common/getDanMu")
    @ApiOperation("获取弹幕")
    public Result<?> getBulletChat() {
        return commentService.getBulletChat();
    }

    @GetMapping("/common/getRecentComment")
    @ApiOperation("获取最近的五条评论")
    public Result<?> getRecentComment() {
        return commentService.getRecentComment();
    }
}
