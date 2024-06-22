package com.llpy.userservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.llpy.entity.Access;
import com.llpy.model.Result;
import com.llpy.userservice.mapper.AccessMapper;
import com.llpy.userservice.redis.RedisService;
import com.llpy.userservice.service.AccessService;
import com.llpy.utils.IPUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 访问服务impl
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Service
public class AccessServiceImpl implements AccessService {

    private final AccessMapper accessMapper;

    private final IPUtils ipUtils;

    private final RedisService redisService;

    public AccessServiceImpl(AccessMapper accessMapper, IPUtils ipUtils, RedisService redisService) {
        this.accessMapper = accessMapper;
        this.ipUtils = ipUtils;
        this.redisService = redisService;
    }

    /**
     * 更新访问
     *
     * @param request 请求
     */
    @Override
    public Result<?> getAccess(HttpServletRequest request) {
        Integer accessSum = redisService.getAccessSum();
        //获得用户ip
        String clientIpAddress = ipUtils.getClientIpAddress(request);
        //根据ip更新count
        Access access = accessMapper.getIpCount(clientIpAddress);
        //更新访问量
        if(access != null){
            access.setCount(access.getCount() + 1);
            accessMapper.updateById(access);
        }else{
            //插入
            Access newAccess = new Access();
            newAccess.setCount(1).setIp(clientIpAddress).setAccessName("网页访问");
            accessMapper.insert(newAccess);
        }
        return Result.success(accessSum);
    }

}
