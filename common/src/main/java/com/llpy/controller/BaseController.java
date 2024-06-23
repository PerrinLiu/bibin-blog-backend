package com.llpy.controller;

import com.llpy.entity.UserDto;
import com.llpy.enums.RedisKeyEnum;
import com.llpy.utils.JsonUtil;
import com.llpy.utils.JwtTokenUtil;
import com.llpy.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


/**
 * 基本控制器
 *
 * @author llpy
 * @date 2024/06/22
 */
public class BaseController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;
    /**
     * 由网关 传入最新的user信息
     *
     * @return
     */
    public UserDto loginUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authToken = request.getHeader(RedisKeyEnum.REDIS_KEY_USER_HEADER_CODE.getKey());
        //处理一些公共请求以及用户登录的情况（比如文章，没登陆也可以查看，登录后可以看到自己是否已点赞）
        if(authToken==null){
            authToken = request.getHeader(RedisKeyEnum.REDIS_KEY_HEADER_INFO.getKey());
            if(authToken==null){
                return new UserDto();
            }
            Object o = redisUtil.get(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey()+authToken);
            if(o==null){
                return new UserDto();
            }
            authToken = o.toString();
        }
        //解析token
        if (!StringUtils.isEmpty(authToken)) {
            //解析token
            String userInfo = jwtTokenUtil.getUsernameFromToken(authToken);
            return JsonUtil.jsonToBean(userInfo, UserDto.class);
        }
        return new UserDto();
    }

}
