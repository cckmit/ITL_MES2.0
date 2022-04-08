package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户表实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 2780604238495419090L;
    @TableId("BO")
    private String bo;

    /**
     * 客户编码
     */
    @TableField("CUSTOMER")
    private String customer;
    /**
     * 客户名称
     */
    @TableField("CUSTOMER_NAME")
    private String customerName;
    /**
     * 客户描述
     */
    @TableField("CUSTOMER_DESC")
    private String customerDesc;
    /**
     * 省/州
     */
    @TableField("STATE_PROV")
    private String stateProv;

    /**
     * 客户地址
     */
    @TableField("ADDRESS1")
    private String address1;
    /**
     * 工厂
     */
    @TableField("SITE")
    private String site;
    /**
     * 国家
     */
    @TableField("COUNTRY")
    private String country;

    /**
     * 邮政编码
     */
    @TableField("POSTAL_CODE")
    private String postalCode;

    /**
     * 电话
     */
    @TableField("TEL")
    private String tel;

    /**
     * 版本
     */
    @TableField("VERSION")
    private String version;

    /**
     * 客户代表
     */
    @TableField("CUSTOMER_REPRESENTATIVE")
    private String customerRepresentative;

    @TableField("EMAIL")
    /**
     * 邮箱
     */
    private String email;

    /**
     * 外部ID
     */
    @TableField("ADDRESS2")
    private String address2;

    /**
     * 联系人
     */
    @TableField("CONTACT")
    private String contact;
    /**
     * 城市
     */
    @TableField("CITY")
    private String city;

    @TableField("CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
    @TableField("CREATE_USER")
    private String createUser;
    @TableField("MODIFY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyDate;
    @TableField("MODIFY_USER")
    private String modifyUser;
}
