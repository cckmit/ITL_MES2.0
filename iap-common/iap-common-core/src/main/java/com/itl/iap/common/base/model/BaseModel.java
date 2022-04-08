package com.itl.iap.common.base.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础实体类
 *
 * @author 胡广
 * @date 2019-08-20
 * @since jdk1.8
 */
@Data
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = -7297711893001046739L;

    /**
     * 启用状态（0-禁用，1-启用）
     */
    public static final String ENABLED = "1";
    public static final String DISABLE = "0";
    /**
     * 删除状态(1已删除0未删除)
     */
    public static final Short IS_DELETE = 1;
    public static final Short IS_NOT_DELETE = 0;
    /**
     * code编码值生成循环跳出次数
     */
    public static final Integer NUM = 20;

    /*租户ID*/
//    private String tenantId;
}
