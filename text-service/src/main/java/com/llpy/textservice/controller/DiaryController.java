package com.llpy.textservice.controller;

import com.llpy.controller.BaseController;
import com.llpy.model.Result;
import com.llpy.textservice.entity.dto.DiaryVo;
import com.llpy.textservice.service.DiaryService;
import com.llpy.textservice.service.Impl.DiaryServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/text")
public class DiaryController extends BaseController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/addDiary")
    public Result addDiary(@RequestBody DiaryVo diaryVo){
        return diaryService.addDiary(diaryVo,loginUser().getUserId());
    }
}
