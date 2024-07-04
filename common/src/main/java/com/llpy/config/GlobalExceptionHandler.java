package com.llpy.config;

import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

/**
 * 全局异常处理程序
 *
 * @author llpy
 * @date 2024/06/08
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    /**
     * 业务异常
     *
     * @param
     * @return
     */
    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException bizException) {
        log.error("业务异常:{}", bizException.getMessage(), bizException);
        return Result.error(bizException.getError());
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        e.printStackTrace();
        return Result.error(ResponseError.COMMON_PARAM_ERROR);
    }


    /**
     * 处理验证异常
     *
     * @param ex 前-
     * @return {@code ResponseEntity<Map<String, String>>}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> collect = new ArrayList<>(ex.getBindingResult().getFieldErrors());
        Map<String, String> map = new HashMap<>();
        map.put("message", collect.get(0).getDefaultMessage());
        return Result.error(ResponseError.COMMON_PARAM_ERROR);
    }

    /**
     * 句柄异常
     *
     * @param e e
     * @return {@code Result<?>}
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointerException(NullPointerException e) {
        e.printStackTrace();
        return Result.error(ResponseError.COMMON_ERROR);
    }

    /**
     * 句柄异常
     *
     * @param e e
     * @return {@code Result<?>}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        e.printStackTrace();
        return Result.error(ResponseError.COMMON_PARAM_ERROR);
    }

    /**
     * 句柄异常
     *
     * @param e e
     * @return {@code Result<?>}
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return Result.error(ResponseError.COMMON_ERROR);
    }


}
