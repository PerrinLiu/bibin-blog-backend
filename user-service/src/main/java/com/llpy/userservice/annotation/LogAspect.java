package com.llpy.userservice.annotation;

import com.alibaba.fastjson.JSON;
import com.llpy.annotation.OperateLog;
import com.llpy.controller.BaseController;
import com.llpy.entity.OperationLog;
import com.llpy.model.Result;
import com.llpy.userservice.mapper.OperationMapper;
import com.llpy.utils.HttpContextUtils;
import com.llpy.utils.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 日志方面
 *
 * @author LLPY
 * @date 2023/11/08
 */
@Aspect
@Component
public class LogAspect extends BaseController {

    //日志接口
    private final OperationMapper operationMapper;
    private final IPUtils ipUtils;

    public LogAspect(OperationMapper operationMapper, IPUtils ipUtils) {
        this.operationMapper = operationMapper;
        this.ipUtils = ipUtils;
    }

    /**
     * 定义切点 @Pointcut
     * 在注解的位置切入代码
     *
     * @date 2021-04-10
     */
    @Pointcut("@annotation(com.llpy.annotation.OperateLog)")
    public void logPointCut() {

    }

    @Around(value = "logPointCut()", argNames = "pjp")
    public Object saveSysLog(ProceedingJoinPoint pjp) throws Throwable {

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        System.out.println("LocalDateTime: " + LocalDateTime.now());

        System.out.println("--------------begin----------------");

        try {
            // 打印请求内容
            System.out.println("url: {" + request.getRequestURL().toString() + "}");
            System.out.println("method: {" + pjp.getSignature() + "}");
            System.out.println("args: {" + Arrays.toString(pjp.getArgs()) + "}");

        } catch (Exception e) {
            System.out.println("###LogAspect.class before() ### ERROR:" + e);
        }

        System.out.println("切面配置通知");

        //创建操作日志对象
        OperationLog log = new OperationLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) pjp.getSignature();

        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
        OperateLog myLog = method.getAnnotation(OperateLog.class);
        if (myLog != null) {
            String value = myLog.value();
            //保存获取的操作
            log.setLogName(value);
        }

        //获取请求的类名
        String className = pjp.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        log.setPath(className + "." + methodName);

        //请求的参数
        Object[] args = pjp.getArgs();
        //将参数所在的数组转换成json
        String params = JSON.toJSONString(args);
        log.setParams(params);
        //获得用户id
        Long userId = loginUser().getUserId();
        //如果id不为空，设置操作日志对象的用户id值
        if (userId != null) {
            log.setUserId(userId);
        }
        String clientIpAddress = ipUtils.getClientIpAddress(request);
        log.setHost(clientIpAddress);

        //执行方法
        Result returnObject = (Result) pjp.proceed(pjp.getArgs());

        //后置通知
        log.setCode(returnObject.getRetCode());
        //获取用户具体操作时间
        log.setTime(LocalDateTime.now());
        //存储至数据库
        operationMapper.insert(log);

        try {
            System.out.println("Response: {}" + JSON.toJSONString(args));
        } catch (Exception e) {
            System.out.println("###LogAspect.class after() ### ERROR:" + e);
        }
        System.out.println("--------------end----------------");

        return returnObject;
    }

}
