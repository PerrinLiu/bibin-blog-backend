package com.llpy.userservice.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.llpy.entity.UserDto;
import com.llpy.enums.RedisKeyEnum;
import com.llpy.model.CodeMsg;
import com.llpy.model.Result;
import com.llpy.userservice.entity.User;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.vo.UserDto2;
import com.llpy.userservice.mapper.UserMapper;
import com.llpy.userservice.service.UserService;
import com.llpy.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Result<UserDto> login(UserLoginQuery userLoginQuery) {
        //根据用户名查找用户
        User user =
                userMapper.selectOne(
                        Wrappers.<User>lambdaQuery()
                                .eq(User::getUsername, userLoginQuery.getUsername()));
        //如果为空，返回用户不存在
        if (user == null) {
            return new Result(CodeMsg.USER_NOT_EXIST);
        } else {
            //存在则验证密码
            // TODO: 2023/7/26 后面把密码都改成加密的
            String password1 = userLoginQuery.getPassword();  //未加密的密码
            String password2 = DigestUtil.sha256Digest(password1);  //加密的密码
            //密码验证不通过，然会密码错误信息
            if (!user.getPassword().equals(password2) && !user.getPassword().equals(password1)) {
                return new Result(CodeMsg.USER_PASS_ERROR);
            }
            //生成一个不带‘ - ‘的uuid，用来和jwt一起存进redis
            String redisUserKey = UUID.randomUUID().toString().replaceAll("-", "");

            //创建要返回的对象
            UserDto userDto = new UserDto();
            userDto.setUserId(user.getUserId()).setToken(redisUserKey);

            //转为json字符串
            String jsonStr = JsonUtil.beanToJson(userDto);

            //生成随机数  增强安全性
            final String randomKey = jwtTokenUtil.getRandomKey();
            //生成token
            final String token = jwtTokenUtil.generateToken(jsonStr, randomKey);

            //存储token至redis
            redisUtil.set(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + redisUserKey, token, RedisKeyEnum.REDIS_KEY_USER_INFO.getExpireTime());

            return Result.success(userDto);
        }
    }

    /**
     * 获取当前登录用户信息
     *
     * @param userId 用户id
     * @return {@link Result}<{@link UserDto2}>
     */
    @Override
    public Result<UserDto2> getUser(Integer userId) {
        return Result.success("成功获取，加油刘林培言");
    }
}
