package com.llpy.userservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.llpy.entity.MenuVo;
import com.llpy.entity.UserDto;
import com.llpy.enums.RedisKeyEnum;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.userservice.design.factory.EmailStrategyFactory;
import com.llpy.userservice.design.strategy.EmailStrategy;
import com.llpy.userservice.entity.User;
import com.llpy.userservice.entity.UserRoles;
import com.llpy.userservice.entity.dto.MailDto;
import com.llpy.userservice.entity.query.UserLoginQuery;
import com.llpy.userservice.entity.dto.UserDto2;
import com.llpy.userservice.entity.dto.UserRegister;
import com.llpy.userservice.mapper.MenuMapper;
import com.llpy.userservice.mapper.UserMapper;
import com.llpy.userservice.mapper.UserRolesMapper;
import com.llpy.userservice.service.UserService;
import com.llpy.userservice.utils.EmailUtil;
import com.llpy.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 用户服务impl
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Service
public class UserServiceImpl implements UserService {
    //用户接口
    private final UserMapper userMapper;

    //jwt工具类
    private final JwtTokenUtil jwtTokenUtil;

    //redis工具类
    private final RedisUtil redisUtil;
    //阿里云oss工具类
    private final AliOSSUtils aliOSSUtils;
    //正则表达式工具类
    private final RegexUtils regexUtils;

    private final MenuMapper menuMapper;

    @Resource
    private UserRolesMapper userRolesMapper;

    private final EmailStrategyFactory emailFactory;
    //ip工具类

    public UserServiceImpl(UserMapper userMapper, JwtTokenUtil jwtTokenUtil, RedisUtil redisUtil, AliOSSUtils aliOssUtils, RegexUtils regexUtils, MenuMapper menuMapper, EmailStrategyFactory emailFactory) {
        this.userMapper = userMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisUtil = redisUtil;
        this.aliOSSUtils = aliOssUtils;
        this.regexUtils = regexUtils;
        this.menuMapper = menuMapper;
        this.emailFactory = emailFactory;
    }

    //默认图片
    private final String DEFAULT_USER_IMG = "https://llpy-blog.oss-cn-shenzhen.aliyuncs.com/userImg/2023-08/defaul.jpg";

    /**
     * 登录
     *
     * @param userLoginQuery 用户登录查询
     * @return {@link Result}
     */
    @Override
    public Result<?> login(UserLoginQuery userLoginQuery, String captchaKey) {
        //拿到前端传的验证码
        String captcha = userLoginQuery.getCaptcha();
        //如果验证码为空
        if (StringUtils.isBlank(captcha)) {
            return Result.error("验证码不能为空");
        }

        //拿出redis的验证码
        String o = (String) redisUtil.get(captchaKey);
        if (o == null || !o.equalsIgnoreCase(userLoginQuery.getCaptcha())) {
            return Result.error(ResponseError.LOGIN_CODE_ERROR);
        }
        //如果验证通过，删除redis中的值
        redisUtil.del(captchaKey);

        //根据用户名查找用户
        User user =
                userMapper.selectOne(
                        Wrappers.<User>lambdaQuery()
                                .eq(User::getUsername, userLoginQuery.getUsername()));
        //如果为空，返回用户不存在
        if (user == null) {
            return Result.error(ResponseError.USER_NOT_EXIST);
        } else {
            //存在则验证密码
            String password = DigestUtil.sha256Digest(userLoginQuery.getPassword());  //加密的密码
            //密码验证不通过，然会密码错误信息
            if (!user.getPassword().equals(password)) {
                return Result.error(ResponseError.USER_PASS_ERROR);
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
            redisUtil.set(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + redisUserKey,
                    token,
                    RedisKeyEnum.REDIS_KEY_USER_INFO.getExpireTime());

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
    public Result<UserDto2> getUser(Long userId) {

        UserDto2 user = userMapper.getUser(userId);
        //将密码设为空再返回
        user.setPassword(null);

        //获取用户所有的权限
        List<MenuVo> userRoot = menuMapper.getUserRoot(userId);
        user.setMenuVos(userRoot);

        return Result.success(user);
    }

    /**
     * 注销
     *
     * @param loginUser 登录用户
     */
    @Override
    public Result<?> logout(UserDto loginUser) {
        redisUtil.del(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + loginUser.getToken());
        return Result.success();
    }


    /**
     * 发送电子邮件
     *
     * @param email 邮件dto
     * @return {@link Result}<{@link ?}>
     */
    @Override
    public Result<?> sendEmail(String email, Integer type) {
        //验证邮箱格式
        if (!regexUtils.isEmail(email)) {
            return Result.error(ResponseError.USER_EMAIL_REGEX_ERROR);
        }


        //根据邮箱查找用户
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getEmail, email));

        EmailStrategy emailStrategy = emailFactory.getEmailStrategy(type);
        if(emailStrategy==null){
            return Result.error(ResponseError.USER_EMAIL_ERROR);
        }

        return emailStrategy.sendEmail(email,type,user);
    }

    /**
     * 电子邮件是真
     *
     * @param emailToken 电子邮件令牌
     * @return {@link Result}<{@link ?}>
     */
    @Override
    public Result<?> emailIsTure(UserRegister userRegister, String emailToken) {
        //验证邮箱验证码
        String redisCode = (String) redisUtil.get(RedisKeyEnum.REDIS_KEY_EMAIL_CODE.getKey() + emailToken);
        String captcha = userRegister.getCaptcha();
        //redis中没有验证码或者验证不通过时返回
        if (redisCode == null || !redisCode.equals(captcha)) {
            return Result.error(ResponseError.USER_EMAIL_CODE_ERROR);
        }
        //验证通过就删除redis中的验证码
        redisUtil.del(RedisKeyEnum.REDIS_KEY_EMAIL_CODE.getKey() + emailToken);

        //返回对应的用户信息
        String email = userRegister.getEmail();

        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getEmail, email);
        User user = userMapper.selectOne(userQuery);

        if (user != null) {
            UserDto2 userDto2 = new UserDto2();
            userDto2.setUsername(user.getUsername())
                    .setNickname(user.getNickname())
                    .setEmail(user.getEmail());
            return Result.success(userDto2);
        }

        //返回用户不存在
        return Result.error(ResponseError.USER_NOT_EXIST);
    }

    @Override
    public Result<?> updatePassword(UserDto2 userDto2) {
        //加密密码
        String password = DigestUtil.sha256Digest(userDto2.getPassword());

        String email = userDto2.getEmail();

        //设置新对象
        User user = new User();
        //设置新密码
        user.setEmail(email).setPassword(password);
        //更新
        LambdaUpdateWrapper<User> userUpdate = new LambdaUpdateWrapper<>();
        userUpdate.eq(User::getEmail, email);
        userMapper.update(user, userUpdate);

        return Result.success();
    }


    /**
     * 注册
     *
     * @param userRegister 用户注册
     * @return {@link Result}
     */
    @Override
    public Result<?> register(UserRegister userRegister, String emailToken) {
        //验证邮箱格式
        if (!regexUtils.isEmail(userRegister.getEmail())) {
            return Result.error(ResponseError.USER_EMAIL_REGEX_ERROR);
        }

        //验证邮箱验证码
        String captcha = (String) redisUtil.get(RedisKeyEnum.REDIS_KEY_EMAIL_CODE.getKey() + emailToken);

        //redis中没有验证码或者验证不通过时返回
        if (captcha == null || !captcha.equals(userRegister.getCaptcha())) {
            //验证码错误
            return Result.error(ResponseError.USER_EMAIL_CODE_ERROR);
        }
        //根据用户名查找用户
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, userRegister.getUsername()));
        //如果存在该用户，返回
        if (user != null) {
            return Result.error(ResponseError.USER_EXIST);
        }

        String password = DigestUtil.sha256Digest(userRegister.getPassword());  //加密密码

        //创建用户对象
        User newUser = new User();
        newUser.setUserId(null);
        newUser.setUsername(userRegister.getUsername())
                .setPassword(password)
                .setEmail(userRegister.getEmail())
                .setGender("男")   //默认性别
                .setNickname(userRegister.getNickname())
                .setUserImg(DEFAULT_USER_IMG)  //给新用户添加默认图片
                .setCreateTime(LocalDateTime.now());
        //插入
        userMapper.insert(newUser);
        //默认权限是普通用户
        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(newUser.getUserId()).setRoleId(3);
        userRolesMapper.insert(userRoles);
        return Result.success("注册成功，可以登录了");
    }


    /**
     * 更新用户
     *
     * @param userDto2 用户dto2
     * @return {@link Result}
     */
    @Override
    public Result updateUser(UserDto2 userDto2) {
        //验证邮箱格式
        boolean email = regexUtils.isEmail(userDto2.getEmail());
        if (!email) {
            return Result.error(ResponseError.USER_EMAIL_REGEX_ERROR);
        }

        //设置新对象
        User user = new User();
        user.setUserId(userDto2.getUserId());

        //如果密码不为空，就代表是更新密码
        if (userDto2.getPassword() != null) {
            //拿到旧密码，验证密码是否相同
            User oldUser = userMapper.selectById(userDto2.getUserId());
            //将密码加密后比较
            String password = DigestUtil.sha256Digest(userDto2.getPassword());
            //如果密码不相同，返回密码错误
            if (!oldUser.getPassword().equals(password)) {
                return Result.success("旧密码错误,请重试");
            }
            //新密码暂存在昵称
            String newPassword = DigestUtil.sha256Digest(userDto2.getNickname());

            //将新密码加密后设置新密码,然后重新将昵称修改会原先的昵称
            user.setNickname(oldUser.getNickname()).setPassword(newPassword);
        } else {
            //否则就是更新用户信息
            user.setNickname(userDto2.getNickname())
                    .setCity(userDto2.getCity())
                    .setEmail(userDto2.getEmail())
                    .setGender(userDto2.getGender());
        }
        user.setUpdateTime(LocalDateTime.now());
        //更新
        userMapper.updateById(user);

        return Result.success("修改成功");
    }

    /**
     * 更新用户img
     *
     * @param file   文件
     * @param userId 用户id
     * @return {@link Result}<{@link ?}>
     */
    @Override
    public Result<?> updateUserImg(MultipartFile file, Long userId) {
        //拿到当前用户信息
        UserDto2 oldUser = getUser(userId).getData();

        //如果不是默认照片，先进行删除操作
        if (oldUser.getUserImg() != null && !oldUser.getUserImg().equals(DEFAULT_USER_IMG)) {
            aliOSSUtils.delete(oldUser.getUserImg());
        }
        try {
            //上传
            String url = aliOSSUtils.upload(file);
            //更新时间
            LocalDateTime updateTime = LocalDateTime.now();
            //新建对象
            User user = new User();
            user.setUserId(userId)
                    .setUserImg(url)
                    .setUpdateTime(updateTime);
            //更新用户信息
            userMapper.updateById(user);
        } catch (IOException e) {
            return Result.error(ResponseError.UPLOAD_IMG_ERROR);
        }
        return Result.success();
    }
}
