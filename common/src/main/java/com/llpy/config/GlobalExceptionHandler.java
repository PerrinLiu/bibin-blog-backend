package com.llpy.config;

import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 全局异常处理程序
 *
 * @author llpy
 * @date 2024/06/08
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> collect = new ArrayList<>(ex.getBindingResult().getFieldErrors());
        Map<String, String> map = new HashMap<>();
        map.put("message", collect.get(0).getDefaultMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleException(NullPointerException e) {
        e.printStackTrace();
        return Result.error(ResponseError.COMMON_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleException(RuntimeException e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}
