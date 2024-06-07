package com.llpy.userservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.llpy.entity.Access;
import com.llpy.model.Result;
import com.llpy.userservice.mapper.AccessMapper;
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

    public AccessServiceImpl(AccessMapper accessMapper, IPUtils ipUtils) {
        this.accessMapper = accessMapper;
        this.ipUtils = ipUtils;
    }

    /**
     * 更新访问
     *
     * @param request 请求
     */
    @Override
    public Result<?> getAccess(HttpServletRequest request) {
        //获得用户ip
        String clientIpAddress = ipUtils.getClientIpAddress(request);

        //查询网页访问的所有ip地址
        QueryWrapper<Access> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("access_name", "网页访问");
        List<Access> accesses = accessMapper.selectList(queryWrapper);

        //遍历数组，查看有没有重复，如果重复，则不插入
        if (accesses == null) {
            return Result.success(0);
        }

        for (Access access : accesses) {
            //如果ip相等，则直接返回当前集合长度，就是访问量
            if (access.getIp().equals(clientIpAddress)) {
                return Result.success(accesses.size());
            }
        }

        //如果没有重复的，代表是新用户，则插入后，将集合长度加1返回
        Access access = new Access();
        access.setAccessName("网页访问");
        access.setIp(clientIpAddress);
        accessMapper.insert(access);

        return Result.success(accesses.size() + 1);
    }

}
