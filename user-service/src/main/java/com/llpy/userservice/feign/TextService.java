package com.llpy.userservice.feign;

import com.llpy.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;

/**
 * 文本服务
 *
 * @author llpy
 * @date 2024/07/03
 */
@FeignClient(name = "textservice", configuration = FeignConfig.class)
public interface TextService {
    /**
     * 获取计数文本
     * String 为key Integer 为数
     * 有对应日期的文章输，以及articleCount,groupCount的数量
     *
     * @return {@code HashMap<String, Integer>}
     */
    @GetMapping("/article/common/getCountText")
    HashMap<String, Integer> getCountText();
}
