package com.llpy.userservice.service;

import com.llpy.model.Result;

import javax.servlet.http.HttpServletRequest;

public interface AccessService {

    Result getAccess(HttpServletRequest request);
}
