package com.llpy.config;

import com.llpy.enums.ResponseError;
import lombok.Getter;

/**
 * 商业例外
 *
 * @author llpy
 * @date 2024/07/03
 */
@Getter
public class BizException extends RuntimeException {
 
    private static final long serialVersionUID = -3229475403587709519L;
 
    private final ResponseError error;

    private String message;
 
    /**
     * 构造器，有时我们需要将第三方异常转为自定义异常抛出，但又不想丢失原来的异常信息，此时可以传入cause
     *
     * @param error
     * @param cause
     */
    public BizException(ResponseError error, Throwable cause) {
        super(cause);
        this.error = error;
    }
 
    /**
     * 构造器，只传入错误枚举
     *
     * @param error
     */
    public BizException(ResponseError error) {
        this.error = error;
    }
}