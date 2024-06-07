package com.llpy.textservice.controller;

import com.llpy.controller.BaseController;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.dto.DiaryVo;
import com.llpy.textservice.service.DiaryService;
import com.llpy.textservice.service.Impl.DiaryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/text")
@Api(tags = {"日记控制类"})
public class DiaryController extends BaseController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/addDiary")
    @ApiOperation(value = "新增日记")
    public Result addDiary(@RequestBody DiaryVo diaryVo) {
        return diaryService.addDiary(diaryVo, loginUser().getUserId());
    }

    @GetMapping("/getDiaryByUser")
    @ApiOperation(value = "获得当前用户所有日记")
    public Result<List<DiaryVo>> getDiaryByUser() {
        return diaryService.getDiary(loginUser().getUserId());
    }

    @GetMapping("/getDiaryAll")
    @ApiOperation(value = "获得所有日记")
    public Result<List<DiaryVo>> getDiary() {
        return diaryService.getDiary();
    }

    @GetMapping("/getDiaryBase")
    @ApiOperation(value = "获取所有基本信息")
    public Result<?> getDiaryBase(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        return diaryService.getDiaryBase(null, pageSize, pageNum);
    }

    @GetMapping("/getDiaryBaseByUser")
    @ApiOperation(value = "获取当前用户日记的基本信息")
    public Result<?> getDiaryBaseByUser(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        return diaryService.getDiaryBase(loginUser().getUserId(), pageSize, pageNum);
    }

    @GetMapping("/getDiaryOne")
    @ApiOperation(value = "获取单个日记信息")
    public Result<DiaryVo> getDiaryOne(@RequestParam Long diaryId) {
        return diaryService.getOneText(diaryId);
    }

    @DeleteMapping("/deleteDiaryOne")
    @ApiOperation(value = "删除单个日记")
    public Result deleteDiaryOne(@RequestParam Long diaryId) {
        return diaryService.deleteDiaryOne(diaryId);
    }
}
