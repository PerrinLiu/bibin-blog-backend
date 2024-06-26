package com.llpy.userservice.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.llpy.entity.Access;
import com.llpy.enums.RedisKeyEnum;
import com.llpy.userservice.entity.User;
import com.llpy.userservice.mapper.AccessMapper;
import com.llpy.utils.RedisUtil;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis服务
 *
 * @author llpy
 * @date 2024/06/22
 */
@Component
public class RedisService {

    private final RedisUtil redisUtil;

    private final AccessMapper accessMapper;

    public RedisService(RedisUtil redisUtil, AccessMapper accessMapper) {
        this.redisUtil = redisUtil;
        this.accessMapper = accessMapper;
    }

    /**
     * 获取总和访问量
     *
     * @return {@code String}
     */
    public Integer getAccessSum() {
        Object o = redisUtil.get(RedisKeyEnum.ACCESS_SUM.getKey());
        if(o == null){
            List<Access> accesses = accessMapper.selectList(null);
            Integer accessSum = 0;
            for (Access access : accesses) {
                accessSum += access.getCount();
            }
            redisUtil.set(RedisKeyEnum.ACCESS_SUM.getKey(), accessSum, RedisKeyEnum.ACCESS_SUM.getExpireTime());
            return accessSum;
        }else {
            return Integer.parseInt(o.toString());
        }
    }


    public String model1(){
        String list;
        List<User> users ;
        Object o = redisUtil.get(RedisKeyEnum.REDIS_KEY_DEIVCE_LIST.getKey());
        if(o==null){
            list = "获取数据保存";
            savaRedis(RedisKeyEnum.REDIS_KEY_DEIVCE_LIST, list, null);
        }else{
            //转换数据
            isRefresh(RedisKeyEnum.REDIS_KEY_DEIVCE_LIST.getKey());
            String jsonString = JSON.toJSONString(o);
            list = jsonString;  //数据不是集合的情况
            users = JSON.parseObject(jsonString, new TypeReference<List<User>>() {  //数据是集合的情况
            });
        }
        return list;
    }


    /**
     * 保存到redis
     *
     * @param redisKeyEnum redis密钥枚举
     * @param value        价值
     * @param key          钥匙
     */
    public static void savaRedis(RedisKeyEnum redisKeyEnum, String value, String key) {
        RedisUtil redisUtil = RedisUtil.getInstance();
        redisUtil.set(redisKeyEnum.getKey(), value);
        redisUtil.expire(redisKeyEnum.getKey(), redisKeyEnum.getExpireTime());
    }

    /**
     * 判断是否需要刷新
     *
     * @param key 钥匙
     */
    private void isRefresh(String key) {
        // TODO: 2024/6/22 待做
    }
}
