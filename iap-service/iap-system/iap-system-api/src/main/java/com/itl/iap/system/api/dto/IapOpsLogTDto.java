package com.itl.iap.system.api.dto;

import java.util.Date;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 操作日志dto
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapOpsLogTDto implements Serializable {
    private static final long serialVersionUID = -43401375759801708L;
    //分页对象
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 创建岗位
     */
    private String createOrg;
    /**
     * 最后更新人
     */
    private String lastUpdateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    /**
     * 最后更新时间
     */
    private Date lastUpdateDate;
    /**
     * 有效期始
     */
    private Date createDateStart;
    /**
     * 有效期至
     */
    private Date createDateEnd;
    /**
     * 方法描述信息
     */
    private String methodDesc;
}