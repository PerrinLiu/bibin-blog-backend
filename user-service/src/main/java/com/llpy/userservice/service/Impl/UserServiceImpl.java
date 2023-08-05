package com.llpy.userservice.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.llpy.entity.UserDto;
import com.llpy.enums.RedisKeyEnum;
import com.llpy.model.CodeMsg;
import com.llpy.model.Result;
import com.llpy.userservice.entity.User;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.vo.UserDto2;
import com.llpy.userservice.entity.vo.UserRegister;
import com.llpy.userservice.mapper.UserMapper;
import com.llpy.userservice.service.UserService;
import com.llpy.utils.*;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisUtil redisUtil;

    public UserServiceImpl(UserMapper userMapper, JwtTokenUtil jwtTokenUtil, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public Result login(UserLoginQuery userLoginQuery) {
        //根据用户名查找用户
        User user =
                userMapper.selectOne(
                        Wrappers.<User>lambdaQuery()
                                .eq(User::getUsername, userLoginQuery.getUsername()));
        //如果为空，返回用户不存在
        if (user == null) {
            return new Result<>(CodeMsg.USER_NOT_EXIST);
        } else {
            //存在则验证密码
            // TODO: 2023/7/26 后面把密码都改成加密的
            String password1 = userLoginQuery.getPassword();  //未加密的密码
            String password2 = DigestUtil.sha256Digest(password1);  //加密的密码
            //密码验证不通过，然会密码错误信息
            if (!user.getPassword().equals(password2) && !user.getPassword().equals(password1)) {
                return new Result<>(CodeMsg.USER_PASS_ERROR);
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
    public Result getUser(Long userId) {
        UserDto2 user = userMapper.getUser(userId);
        return Result.success(user);
    }

    /**
     * 注销
     *
     * @param loginUser 登录用户
     */
    @Override
    public Result logout(UserDto loginUser) {
        redisUtil.del(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey()+loginUser.getToken());
        return Result.success();
    }

    @Override
    public Result updateUser(UserDto2 userDto2) {
        User user = new User();
        user.setUserId(userDto2.getUserId());
        user.setNickname(userDto2.getNickname());
        user.setCity(userDto2.getCity());
        user.setEmail(userDto2.getEmail());
        user.setGender(userDto2.getGender());
        userMapper.updateById(user);
        return Result.success("修改成功");
    }

    @Override
    public Result register(UserRegister userRegister) {
        //根据用户名查找用户
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                        .eq(User::getUsername, userRegister.getUsername()));
        if(user!=null){
            return new Result<>(CodeMsg.USER_EXIST);
        }
        String password = DigestUtil.sha256Digest(userRegister.getPassword());  //加密密码
        //创建用户对象
        User newUser = new User();
        newUser.setUserId(null);
        newUser.setUsername(userRegister.getUsername());
        newUser.setPassword(password);
        newUser.setNickname(userRegister.getNickname());
        userMapper.insert(newUser);
        return Result.success("注册成功，可以登录了");
    }
}
