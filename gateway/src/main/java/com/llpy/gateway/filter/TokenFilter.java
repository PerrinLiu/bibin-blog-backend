package com.llpy.gateway.filter;

import com.google.gson.Gson;
import com.llpy.entity.UserDto;
import com.llpy.enums.GatewayKey;
import com.llpy.enums.RedisKeyEnum;
import com.llpy.gateway.res.JwtResponse;
import com.llpy.utils.DateUtils;
import com.llpy.utils.JwtTokenUtil;
import com.llpy.utils.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Date;

@Slf4j
@Component
public class TokenFilter implements GlobalFilter, Ordered {

    private final RedisUtil redisUtil;

    private final JwtTokenUtil jwtTokenUtil;

    /**
     * JWT 失效时间小于30分钟，更新JWT
     */
    private final static Long EXPIRED_JWT = 30 * 60L;

    /**
     * redis 30 分钟会话失效时间
     */
    private final static Long EXPIRED_REDIS = 30 * 60L;

    public TokenFilter(RedisUtil redisUtil, JwtTokenUtil jwtTokenUtil) {
        this.redisUtil = redisUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    /**
     * 滤器
     *
     * @param exchange 交换
     * @param chain    链条
     * @return {@link Mono}<{@link Void}>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //设置响应体为json格式
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");

        //添加请求头，防止用户绕过网关访问微服务
        ServerHttpRequest request1 = exchange.getRequest();
        request1.mutate().header(GatewayKey.GATEWAY_KEY.getKey(), GatewayKey.GATEWAY_KEY.getKeyInfo());

        //拿到请求对象
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        //获取请求头的RedisKeyEnum.REDIS_KEY_HEADER_INFO  也就是X-Token
        String uuid = serverHttpRequest.getHeaders().getFirst(RedisKeyEnum.REDIS_KEY_HEADER_INFO.getKey());

        //准备用来存储token
        String authToken;
        try {
            //获取请求的接口
            String path = serverHttpRequest.getURI().getPath();
            log.info("当前请求接口：" + path);

            //直接可请求的资源
            String[] ignoresUrl = {
                    "login", "register", "getDiaryAll",
                    "getAccess", "getDiaryOne", "getDiaryBase",
                    "sendEmail", "swagger-ui.html", "api-docs",
                    "captcha", "generate-base64", "emailIsTrue",
                    "updatePassword"
            };

            //提取最后一个'/'的子串
            int index = path.lastIndexOf("/");
            path = path.substring(index + 1);

            for (String url : ignoresUrl) {
                //如果是直接可请求的资源
                if (path.equals(url)) {
                    //放行
                    return chain.filter(exchange);
                }

            }
            //判断请求头是否空或空白
            if (StringUtils.isBlank(uuid)) {

                return JwtResponse.jwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "token出错");

            } else {
                //从缓存中获取jwt
                Object sessionJwt = redisUtil.get(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + uuid);
                if (sessionJwt == null) {
                    //如果为空，返回无效
                    return JwtResponse.jwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "token is Invalidation");
                }

                //将其转为字符串
                authToken = String.valueOf(sessionJwt);
                //获取到用户信息
                String userInfo = jwtTokenUtil.getUsernameFromToken(authToken);

                //反序列化转为UserDto对象
                UserDto loginUser = new Gson().fromJson(userInfo, UserDto.class);
                //获取jwt失效时间
                Date getExpirationDateFromToken = jwtTokenUtil.getExpirationDateFromToken(String.valueOf(sessionJwt));
                long remainingMinutes = DateUtils.getMinuteDifference(getExpirationDateFromToken, DateUtils.getCurrentTime());

                //如果小于半小时，刷新JWT
                if (remainingMinutes <= EXPIRED_JWT) {
                    // randomKey和token已经生成完毕
                    final String randomKey = jwtTokenUtil.getRandomKey();
                    final String newToken = jwtTokenUtil.generateToken(userInfo, randomKey);

                    //保存新的token
                    redisUtil.set(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + loginUser.getToken(),
                            newToken,
                            RedisKeyEnum.REDIS_KEY_USER_INFO.getExpireTime());
                    authToken = newToken;
                }
                //小于半小时刷新Redis时间
                long expireTime = redisUtil.getExpire(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + loginUser.getToken());
                if (expireTime <= EXPIRED_REDIS) {
                    //刷新redis时间
                    redisUtil.expire(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + loginUser.getToken(),
                            RedisKeyEnum.REDIS_KEY_USER_INFO.getExpireTime());
                }

                ServerHttpRequest request = exchange
                        .getRequest().mutate()
                        // 统一头部，用于防止直接调用服务
                        .header(RedisKeyEnum.REDIS_KEY_USER_HEADER_CODE.getKey(), authToken)
                        .header("exit", uuid)
                        .build();
                exchange.getResponse().getHeaders().add("testresponse", "testresponse");
                ServerWebExchange webExchange = exchange.mutate().request(request).build();
                //放行
                return chain.filter(webExchange);
            }
        } catch (MalformedJwtException e) {
            log.error("JWT格式错误异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.jwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "JWT格式错误");
        } catch (SignatureException e) {
            log.error("JWT签名错误异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.jwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "JWT签名错误");
        } catch (ExpiredJwtException e) {
            log.error("JWT过期异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.jwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "会话已失效，请重新登录");
        } catch (UnsupportedJwtException e) {
            log.error("不支持的JWT异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.jwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "JWT格式不正确");
        } catch (Exception e) {
            log.error("TokenFilter，不支持的异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.jwtResponse(exchange, HttpStatus.NOT_ACCEPTABLE.value(), "TokenFilter：token 解析异常:");
        }
    }


    @Override
    public int getOrder() {
        return -99;
    }
}
