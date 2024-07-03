package com.llpy.model;

import com.llpy.enums.ResponseError;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;

/**
 * 后果
 *
 * @author llpy
 * @date 2024/07/02
 */
@Data
@ApiOperation(value = "通用返回对象")
public class Result<T> implements Serializable {

    private String message;
    private int retCode;
    private T data;

    public Result(String message, int retCode, T data) {
        this.message = message;
        this.retCode = retCode;
        this.data = data;
    }


    public static <T> Result<T> success(T data) {
        return new Result<>("成功", 200, data);
    }

    public static <T> Result<T>  success() {
        return new Result<>("成功", 200, null);
    }

    public static  <T> Result<T> error(String message) {
        return new Result<>(message, 500, null);
    }

    public static  <T> Result<T> error(ResponseError responseError) {
        return new Result<>(responseError.getMessage(), responseError.getCode(), null);
    }

}