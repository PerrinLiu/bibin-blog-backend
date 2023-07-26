package com.llpy.textservice.controller;

import com.llpy.model.Result;
import com.llpy.textservice.entity.Article;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/text")
public class TextController {
    @GetMapping("/getText")
    public Result getTest(){
        Article article = new Article();
        article.setText("文章");
        return Result.success(article);
    }
}
