package com.llpy.textservice.controller;


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
    public Result<?> updateFile(@RequestParam("file") MultipartFile file) {
        return imagesService.uploadImg(file,loginUser().getUserId());
    }

    @GetMapping("/listImgByUserId")
    public Result<?> getImg(@RequestParam Long userId) {
        return imagesService.listImgByUserId(userId);
    }
}
