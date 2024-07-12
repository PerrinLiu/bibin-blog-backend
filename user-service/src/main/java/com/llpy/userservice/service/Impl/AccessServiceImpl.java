package com.llpy.userservice.service.Impl;

import com.llpy.entity.Access;
import com.llpy.model.Result;
import com.llpy.userservice.entity.vo.CountDataVo;
import com.llpy.userservice.feign.TextService;
import com.llpy.userservice.mapper.AccessMapper;
import com.llpy.userservice.redis.RedisService;
import com.llpy.userservice.service.AccessService;
import com.llpy.utils.DataUtils;
import com.llpy.utils.IPUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    private final ThreadPoolTaskExecutor taskExecutor;

    private final TextService textService;


    public AccessServiceImpl(AccessMapper accessMapper, IPUtils ipUtils, RedisService redisService, @Qualifier("taskExecutor")  ThreadPoolTaskExecutor taskExecutor, TextService textService) {
        this.accessMapper = accessMapper;
        this.ipUtils = ipUtils;
        this.redisService = redisService;
        this.taskExecutor = taskExecutor;
        this.textService = textService;
    }

    /**
     * 更新访问
     *
     * @param request 请求
     */
    @Override
    public void getAccess(HttpServletRequest request) {
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
    }

    @Override
    public Result<?> getCountData(Integer day) {
        //异步获取文章统计数据
        Future<HashMap<String,Integer>> submit = taskExecutor.submit(textService::getCountText);
        CountDataVo res = new CountDataVo();
        // 新建commitData集合
        List<CountDataVo.CommitData> commitDataList = new ArrayList<>();
        // 获取网站访问量
        Integer accessSum = redisService.getAccessSum();
        res.setVisitCount(accessSum);
        try {
            //获取异步结果，设置对应的数据
            HashMap<String,Integer> map = submit.get();
            res.setArticleCount(map.get("articleCount"));
            res.setGroupCount(map.get("groupCount"));
            res.setDiaryCount(map.get("diaryCount"));
            String[] arrByDay = DataUtils.getArrByDay(day);
            for (String s : arrByDay) {
                CountDataVo.CommitData commitData = new CountDataVo.CommitData();
                //判断是否为今天
                commitData.setToday(s.equals(DataUtils.getToday()));
                //设置日期
                commitData.setYear(s.substring(0,4)).setMonth(s.substring(5,7)).setDate(s.substring(8,10));
                //设置数量和等级
                Integer orDefault = map.getOrDefault(s, 0);
                commitData.setNumber(orDefault).setLevel(orDefault >= 3 ? 3 : orDefault);
                commitDataList.add(commitData);
            }
            //设置commitData
            res.setCommitDataList(commitDataList);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }


        // 半年前的日期
        String[] monthBar = new String[24]; // 存储24列的月份信息
        setMonBar(monthBar,day);
        res.setMonthBar(monthBar);
        return Result.success(res);
    }

    private static void setMonBar(String[] monthBar,Integer day) {
        Arrays.fill(monthBar, "");

        // 获取今天的日期
        LocalDate current = LocalDate.now();
        // 最近的日期（根据传进来的day）
        LocalDate startDate = current.minusDays(day); // 向前推183天，加上今天，总共184天

        for (int i = 0; i < day ; i++) {
            // 计算每次循环的日期
            LocalDate d = startDate.plusDays(i);

            // 判断每月第一天在12列中的哪一列
            if (d.getDayOfMonth() == 1) {
                int month = d.getMonthValue(); // 获取月份
                int weekOfMonth = i / 7; // 计算当前是第几周
                if (weekOfMonth < monthBar.length) {
                    monthBar[weekOfMonth-1 < 0 ? 0 : weekOfMonth] = month + "月";
                }
            }
        }
    }
}
