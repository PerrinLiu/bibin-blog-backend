package com.llpy.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private String message;
    private int retCode;
    private T data;

    public Result(String message, int retCode, T data) {
        this.message = message;
        this.retCode = retCode;
        this.data = data;
    }
    public Result(CodeMsg codeMsg) {
        this.message = codeMsg.getMessage();
        this.retCode = codeMsg.getRetCode();
    }
    public static Result success(Object data)
    {
        return new Result("成功",200,data);
    }
    public static Result success()
    {
        return new Result("成功",200,null);
    }
    public static Result error(String message)
    {
        return new Result(message,500,null);
    }



}