package com.itl.iap.common.base.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求响应包装
 *
 * @author Linjl
 * @date 2020-06-12
 */
@Data
@Slf4j
public class ResponseData<T> {

    private static final long serialVersionUID = -5816713866887895850L;
    /**
     * 错误码
     */
    private String code = ResultResponseEnum.ERROR.getCode();

    /**
     * 错误信息
     */
    private String msg = null;

    /**
     * 返回结果实体
     */
    private T data = null;

    /**
     * 无参构造函数
     */
    public ResponseData() {
    }

    /**
     * 有参构造函数
     *
     * @param code 状态码
     * @param msg  消息
     */
    public ResponseData(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 有参构造函数
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     */
    public ResponseData(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回默认错误
     *
     * @param msg 消息
     * @param <T> 泛型
     * @return
     */
    public static <T> ResponseData<T> error(String msg) {
        log.debug("返回默认错误：, msg={}", msg);
        return new ResponseData<T>(ResultResponseEnum.ERROR.getCode(), msg, null);
    }

    /**
     * 返回默认错误
     *
     * @param bool 消息
     * @param <T> 泛型
     * @return
     */
    public static <T> ResponseData<T> error(T bool) {
        log.debug("返回默认错误：, msg={}", bool);
        return new ResponseData<T>(ResultResponseEnum.ERROR.getCode(), "", bool);
    }

    /**
     * 返回错误
     *
     * @param resultEnum 返回结果枚举类
     * @param <T>        泛型
     * @return ResponseData
     */
    public static <T> ResponseData<T> error(ResultResponseEnum resultEnum) {
        log.debug("返回错误：code={}, msg={}", resultEnum.getCode(), resultEnum.getDesc());
        return new ResponseData<T>(resultEnum.getCode(), resultEnum.getDesc(), null);
    }

    /**
     * 返回错误
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  泛型
     * @return
     */
    public static <T> ResponseData<T> error(String code, String msg) {
        log.debug("返回错误：code={}, msg={}", code, msg);
        return new ResponseData<T>(code, msg, null);
    }

    /**
     * 返回成功
     *
     * @param data 数据
     * @param <T>  泛型
     * @return ResponseData
     */
    public static <T> ResponseData<T> success(T data) {
        return new ResponseData<T>(ResultResponseEnum.SUCCESS.getCode(), "success", data);
    }

    /**
     * 返回成功
     *
     * @param <T> 泛型
     * @return ResponseData
     */
    public static <T> ResponseData<T> success() {
        return new ResponseData<T>(ResultResponseEnum.SUCCESS.getCode(), "success", null);
    }
}
