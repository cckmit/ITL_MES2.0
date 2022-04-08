package com.itl.iap.mes.provider.common;

import lombok.Data;

/**
 * 响应状态
 * @author 胡广
 * @version 1.0
 * @name BaseState
 * @description
 * @date 2019-07-03
 */
@Data
public class BaseState {
    private String code;
    private String message;
}
