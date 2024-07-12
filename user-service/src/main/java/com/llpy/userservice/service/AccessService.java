package com.llpy.userservice.service;

import com.llpy.model.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * 接入服务
 *
 * @author LLPY
 * @date 2023/11/08
 */
public interface AccessService {

    /**
     * 访问页面
     *
     * @param request 要求
     */
    void getAccess(HttpServletRequest request);

    /**
     * 获取网站统计数据
     *
     * @param day 白天
     * @return {@code Result<?>}
     */
    Result<?> getCountData(Integer day);

}
