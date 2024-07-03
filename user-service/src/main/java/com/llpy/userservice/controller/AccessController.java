package com.llpy.userservice.controller;


import com.llpy.model.Result;
import com.llpy.userservice.service.AccessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问控制器
 *
 * @author LLPY
 * @date 2023/11/08
 */
@RestController
@RequestMapping("/user")
@Api(tags = {"统计访问类"})
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @GetMapping("/getAccess")
    @ApiOperation(value = "访问系统")
    public void getAccess(HttpServletRequest request) {
        accessService.getAccess(request);
    }


    @GetMapping("/common/getCountData")
    @ApiOperation(value = "获取统计数据")
    public Result<?> getCountData() {
        return accessService.getCountData();
    }


}
