package com.llpy.textservice.feign;


import com.llpy.config.FeignConfig;
import com.llpy.textservice.feign.entity.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

/**
 * 用户服务
 *
 * @author llpy
 * @date 2024/06/30
 */
@FeignClient(name = "userservice", configuration = FeignConfig.class)
public interface  UserService {
    @GetMapping("/user/common/getUserData")
    HashMap<Long, UserVo> getUserData();
}
