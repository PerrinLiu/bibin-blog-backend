package com.llpy.textservice.controller;


import com.llpy.annotation.OperateLog;
import com.llpy.controller.BaseController;
import com.llpy.model.Result;
import com.llpy.textservice.service.ImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 静态资源控制器
 *
 * @author llpy
 * @date 2024/06/25
 */
@RestController
@RequestMapping("/image")
@Api(tags = "图片资源控制器")
public class ImagesController extends BaseController {

    private final ImagesService imagesService;

    public ImagesController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @PostMapping("/uploadFile")
    @ApiOperation(value = "上传图片")
    @OperateLog("上传图片")
    public Result<?> updateFile(@RequestParam("file") MultipartFile file) {
        return imagesService.uploadImg(file,loginUser().getUserId());
    }

    @GetMapping("/common/groupCountByUser")
    @ApiOperation(value = "统计每个用户的上传数量")
    public Result<?> getCountImg(@RequestParam Integer pageSize, @RequestParam Integer pageNum,@RequestParam String searchText) {
        return imagesService.listCountImg(pageSize,pageNum,searchText);
    }


    @GetMapping("/common/listImgByUserId")
    @ApiOperation(value = "获得用户上传的图片")
    public Result<?> getImg(@RequestParam Long userId) {
        return imagesService.listImgByUserId(userId);
    }

    @GetMapping("/listImgByUser")
    @ApiOperation(value = "获取自己的上传的图片")
    public Result<?> getImg() {
        return imagesService.listImgByUser(loginUser().getUserId());
    }

    @DeleteMapping("/deleteImg")
    @ApiOperation(value = "删除图片")
    @OperateLog("删除图片")
    public Result<?> deleteImg(@RequestParam Long imgId) {
        return imagesService.deleteImg(imgId, loginUser().getUserId());
    }

    @PutMapping("/openOrCloseImg")
    @ApiOperation(value = "开启或关闭图片")
    public Result<?> openOrCloseImg(@RequestParam Long imgId) {
        return imagesService.openOrCloseImg(imgId);
    }
}
