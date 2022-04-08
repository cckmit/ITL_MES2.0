package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 接口管理实体类
 *
 * @author 马家伦
 * @date 2020-06-22
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_sys_api_t")
@Accessors(chain = true)
public class IapSysApiT {

    /**
     * 主键id
     */
    @TableId
    private String id;

    /**
     * 系统代码
     */
    @TableField("system_code")
    private String systemCode;

    /**
     * 模块名称
     */
    @TableField("model_name")
    private String modelName;

    /**
     * 类名
     */
    @TableField("class_name")
    private String className;

    /**
     * 类描述信息
     */
    @TableField("class_desc")
    private String classDesc;

    /**
     * 类url
     */
    @TableField("class_url")
    private String classUrl;

    /**
     * 类中的方法名称
     */
    @TableField("method_name")
    private String methodName;

    /**
     * 方法描述信息
     */
    @TableField("method_desc")
    private String methodDesc;

    /**
     * 方法url
     */
    @TableField("method_url")
    private String methodUrl;

    /**
     * 请求方式
     */
    @TableField("request_type")
    private String requestType;

    /**
     * 启用/禁用（0-启用， 1- 禁用）
     */
    @TableField("enabled")
    private Short enabled;

    /**
     * 校验（0-已校验，1-未校验）
     */
    @TableField("checked")
    private Short checked;

    /**
     * 更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;


}
