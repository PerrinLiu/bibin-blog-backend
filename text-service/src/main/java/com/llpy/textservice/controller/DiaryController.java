package com.llpy.textservice.controller;

import com.llpy.controller.BaseController;
import com.llpy.model.Result;
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
    @ApiOperation(value = "新增文章")
    public Result addDiary(@RequestBody DiaryVo diaryVo){
        return diaryService.addDiary(diaryVo,loginUser().getUserId());
    }

    @GetMapping("/getDiary")
    @ApiOperation(value = "获得当前用户所有文章")
    public Result<List<DiaryVo>> getDiary(){
        return diaryService.getDiary(loginUser().getUserId());
    }
}
