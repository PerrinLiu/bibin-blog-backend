package com.llpy.userservice.controller;


import com.llpy.model.Result;
import com.llpy.userservice.service.AccessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Api(tags = {"统计访问类"})
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @GetMapping("/getAccess")
    @ApiOperation(value="统计网站访问")
    public Result getAccess(HttpServletRequest request){
        return accessService.getAccess(request);
    }
}
