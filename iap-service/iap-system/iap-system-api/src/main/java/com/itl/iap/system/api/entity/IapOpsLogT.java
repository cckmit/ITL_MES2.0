package com.itl.iap.system.api.entity;

import java.util.Date;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 操作日志实体类
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_ops_log_t")
public class IapOpsLogT extends BaseModel {
    private static final long serialVersionUID = -43057498470208535L;

    @TableId
    private String id;

    @TableField("service_id")
    private String serviceId;

    @TableField("service_name")
    private String serviceName;

    @TableField("service_ip")
    private String serviceIp;

    @TableField("namespace")
    private String namespace;
    @TableField("method_type")
    private Short methodType;
    @TableField("request_function")
    private String requestFunction;
    @TableField("request_method")
    private String requestMethod;
    @TableField("request_proxy")
    private String requestProxy;
    @TableField("request_params")
    private String requestParams;
    @TableField("log_data")
    private String logData;
    @TableField("creater")
    private String creater;
    @TableField("create_date")
    private Date createDate;
    @TableField("create_org")
    private String createOrg;
    @TableField("last_update_by")
    private String lastUpdateBy;
    @TableField("last_update_date")
    private Date lastUpdateDate;
    @TableField("method_desc")
    private String methodDesc;
}