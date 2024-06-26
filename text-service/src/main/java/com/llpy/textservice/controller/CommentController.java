package com.llpy.textservice.controller;


import com.llpy.model.Result;
import com.llpy.textservice.entity.dto.CommentDto;
import com.llpy.textservice.service.CommentService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * 评论控制器
 *
 * @author llpy
 * @date 2024/06/26
 */
@RestController
@RequestMapping("/article")
@Api(tags = "评论控制类")
public class CommentController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/listComment")
    public Result<?> listComment(@RequestParam Long articleId,@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        return commentService.listComment(articleId,pageSize,pageNum);
    }

    @PostMapping("/addComment")
    public Result<?> addComment(@RequestBody CommentDto commentDto) {
        return commentService.addComment(commentDto);
    }
}
