package com.itl.iap.common.base.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * 常见异常类
 *
 * @author 汤俊
 * @date 2020-4-15 16:15
 * @since jdk1.8
 */
@Data
public class CommonException extends Exception implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态码
     */
    private String message;

    /**
     * 常见异常
     */
    public CommonException() {
        super();
    }

    /**
     * 有参构造函数
     *
     * @param message 消息
     * @param code    返回状态码
     */
    public CommonException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    /**
     * 有参构造函数
     *
     * @param definition 自定义异常定义类
     */
    public CommonException(CommonExceptionDefinition definition) {
        super(definition.getMsg());
        this.code = definition.getCode();
    }
}
