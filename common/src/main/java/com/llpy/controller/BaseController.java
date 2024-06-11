package com.llpy.controller;

import com.llpy.entity.UserDto;
import com.llpy.enums.RedisKeyEnum;
import com.llpy.utils.JsonUtil;
import com.llpy.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


public class BaseController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 由网关 传入最新的user信息
     *
     * @return
     */
    public UserDto loginUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authToken = request.getHeader(RedisKeyEnum.REDIS_KEY_USER_HEADER_CODE.getKey());
        //解析token
        if (!StringUtils.isEmpty(authToken)) {
            //解析token
            String userInfo = jwtTokenUtil.getUsernameFromToken(authToken);
            UserDto empAccount = JsonUtil.jsonToBean(userInfo, UserDto.class);
            return empAccount;
        }
        return new UserDto();
    }

}
