package com.itl.iap.mes.provider.common;

import lombok.Data;

/**
 * 响应DTO基类
 * @author 胡广
 * @version 1.0
 * @name BaseResponse
 * @description
 * @date 2019-07-03
 */
@Data
public class BaseResponse<T> {
    private Boolean success;
    private BaseState state;
    private T data;
}
