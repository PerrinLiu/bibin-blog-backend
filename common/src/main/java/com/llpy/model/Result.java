package com.llpy.model;

import com.llpy.enums.ResponseError;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiOperation(value = "查看指定用户权限信息")
public class Result<T> implements Serializable {

    private String message;
    private int retCode;
    private T data;

    public Result(String message, int retCode, T data) {
        this.message = message;
        this.retCode = retCode;
        this.data = data;
    }


    public static Result success(Object data) {
        return new Result<>("成功", 200, data);
    }

    public static Result success() {
        return new Result<>("成功", 200, null);
    }

    public static Result error(String message) {
        return new Result<>(message, 500, null);
    }

    public static Result error(ResponseError responseError) {
        return new Result<>(responseError.getMessage(), responseError.getCode(), null);
    }

}