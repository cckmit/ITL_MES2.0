package com.itl.iap.common.base.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (IapOpsLogT)DTO类
 *
 * @author iAP
 * @since 2020-06-28 14:42:11
 */
@Data
@Accessors(chain = true)
public class IapOpsLogTDto implements Serializable {

    private static final long serialVersionUID = -16998660951576255L;

    /**
     * 方法类型定义：(0:接口日志,1:异常日志)
     */
    public static final Short METHOD_TYPE0 = 0;
    public static final Short METHOD_TYPE1 = 1;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 服务器ID
     */
    private String serviceId;

    /**
     * 服务器名称
     */
    private String serviceName;

    /**
     * 服务器IP
     */
    private String serviceIp;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 方法类型 (0:接口日志,1:异常日志)
     */
    private Short methodType;

    /**
     * 请求方式
     */
    private String requestFunction;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求代理
     */
    private String requestProxy;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求参数
     */
    private String logData;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建岗位
     */
    private String createOrg;

    /**
     * 最后更新人
     */
    private String lastUpdateBy;

    /**
     * 最后更新时间
     */
    private Date lastUpdateDate;

    /**
     * 方法描述信息
     */
    private String methodDesc;
}