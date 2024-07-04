package com.llpy.textservice.controller;

import com.llpy.controller.BaseController;
import com.llpy.model.Result;
import com.llpy.textservice.entity.vo.DiaryVo;
import com.llpy.textservice.service.DiaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日记控制器
 *
 * @author llpy
 * @date 2024/06/23
 */
@RestController
@RequestMapping("/diary")
@Api(tags = {"日记控制类"})
public class DiaryController extends BaseController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/addDiary")
    @ApiOperation(value = "新增日记")
    public Result<?> addDiary(@RequestBody DiaryVo diaryVo) {
        return diaryService.addDiary(diaryVo, loginUser().getUserId());
    }

    @GetMapping("/getDiaryAll")
    @ApiOperation(value = "获得所有日记")
    public Result<?> getDiaryAll(@RequestParam Integer pageSize, @RequestParam Integer pageNum, Long userId,Integer status,String searchText) {
        return diaryService.getDiary(userId,pageSize,pageNum,status,searchText);
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


    @PutMapping("/rejectDiary")
    @ApiOperation(value = "拒绝日记")
    public Result<?> rejectDiary(@RequestParam Long diaryId,@RequestParam String rejectReason) {
        return diaryService.rejectDiary(diaryId,rejectReason,loginUser().getUserId());
    }

    @PutMapping("/passDiary")
    @ApiOperation(value = "通过日记")
    public Result<?> passDiary(@RequestParam Long diaryId) {
        return diaryService.passDiary(diaryId,loginUser().getUserId());
    }
}
