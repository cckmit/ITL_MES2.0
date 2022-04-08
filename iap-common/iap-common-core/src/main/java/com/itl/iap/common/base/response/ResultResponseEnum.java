package com.itl.iap.common.base.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回的code统一管理枚举类
 *
 * @author 汤俊
 * @date 2020-06-09
 * @since jdk1.8
 */
@Getter
@AllArgsConstructor
public enum ResultResponseEnum {
    /**
     * 成功
     */
    SUCCESS("200", "成功"),

    /**
     * 默认错误
     */
    ERROR("999", "错误"),

    /**
     * 第三方服务异常
     */
    THIRD_PART_SERVICE_EXCEPTION("0", "第三方服务异常"),

    /**
     * 系统异常,请联系管理员
     */
    DEFAULT_SERVICE_EXCEPTION("100000", "系统异常,请联系管理员"),

    /**
     * 系统运行时异常,请联系管理员
     */
    DEFAULT_SERVICE_RUNTIME_EXCEPTION("100001", "系统运行时异常,请联系管理员");

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;
}
