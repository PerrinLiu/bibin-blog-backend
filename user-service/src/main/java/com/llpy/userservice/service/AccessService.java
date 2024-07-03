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

    void getAccess(HttpServletRequest request);

    Result<?> getCountData();

}
