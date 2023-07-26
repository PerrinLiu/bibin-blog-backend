package com.llpy.photoservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/photo")
public class PhotoController {
    @GetMapping("/1")
    public String getPhoto(){
        return "照片";
    }
}
