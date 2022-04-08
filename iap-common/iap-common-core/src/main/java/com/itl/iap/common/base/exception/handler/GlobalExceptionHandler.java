package com.itl.iap.common.base.exception.handler;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类（ handler ）
 *
 * @author 胡广
 * @name GlobalExceptionHandler
 * @date 2019-07-1616:07
 * @since jdk1.8
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 无参构造函数
     */
    public GlobalExceptionHandler() {
        log.info("Initialized GlobalExceptionHandler");
    }

    /**
     * 常见异常处理方法
     *
     * @param ex
     * @return ResponseData
     */
    @ExceptionHandler(CommonException.class)
    public ResponseData commonExceptionHandler(CommonException ex) {
        log.error(ex.getMessage());
        return ResponseData.error(String.valueOf(ex.getCode()), ex.getMessage());
    }

    /**
     * 默认异常处理方法
     *
     * @param ex
     * @return ResponseData
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseData defaultErrorHandler(Exception ex) {
        log.error(ex.getMessage());
        return ResponseData.error(ResultResponseEnum.DEFAULT_SERVICE_EXCEPTION.getCode(), ex.getMessage());
    }

    /**
     * 默认异常处理方法
     *
     * @param ex
     * @return ResponseData
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseData defaultErrorHandler(RuntimeException ex) {
        log.error(ex.getMessage());
        return ResponseData.error(ResultResponseEnum.DEFAULT_SERVICE_RUNTIME_EXCEPTION.getCode(), ResultResponseEnum.DEFAULT_SERVICE_RUNTIME_EXCEPTION.getDesc());
    }

}
