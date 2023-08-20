package com.llpy.textservice.controller;

import com.llpy.controller.BaseController;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.dto.DiaryVo;
import com.llpy.textservice.service.DiaryService;
import com.llpy.textservice.service.Impl.DiaryServiceImpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/text")
public class DiaryController extends BaseController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/addDiary")
    @ApiOperation(value = "新增日记")
    public Result addDiary(@RequestBody DiaryVo diaryVo){
        return diaryService.addDiary(diaryVo,loginUser().getUserId());
    }

    @GetMapping("/getDiaryByUser")
    @ApiOperation(value = "获得当前用户所有日记")
    public Result<List<DiaryVo>> getDiaryByUser(){
        return diaryService.getDiary(loginUser().getUserId());
    }

    @GetMapping("/getDiaryAll")
    @ApiOperation(value = "获得所有日记")
    public Result<List<DiaryVo>> getDiary(){
        return diaryService.getDiary();
    }

    @GetMapping("/getDiaryBase")
    @ApiOperation(value = "获取所有基本信息")
    public Result<List<Diary>> getDiaryBase(){
        return diaryService.getDiaryBase(null);
    }

    @GetMapping("/getDiaryBaseByUser")
    @ApiOperation(value = "获取当前用户日记的基本信息")
    public Result<List<Diary>> getDiaryBaseByUser(){
        return diaryService.getDiaryBase(loginUser().getUserId());
    }

    @GetMapping("/getDiaryOne")
    @ApiOperation(value = "获取单个日记信息")
    public Result<DiaryVo> getDiaryOne(@RequestParam Long diaryId){
        return diaryService.getOneText(diaryId);
    }
}
