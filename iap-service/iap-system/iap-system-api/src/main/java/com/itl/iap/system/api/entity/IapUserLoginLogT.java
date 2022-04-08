package com.itl.iap.system.api.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录日志实体类
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_user_login_log_t")
public class IapUserLoginLogT extends BaseModel {
    private static final long serialVersionUID = 205093470736347768L;

    @TableId
    private String id;

    @TableField("user_id")
    private String userId;
    /**
     * 登陆方式(登录方式 第三方/邮箱/手机等)
     */
    @TableField("login_type")
    private Short loginType;
    /**
     * 操作类型 1登陆成功  2登出成功 3登录失败 4登出失败
     */
    @TableField("command")
    private Short command;

    @TableField("version")
    private String version;

    @TableField("client")
    private String client;

    @TableField("device_id")
    private String deviceId;

    @TableField("last_ip")
    private String lastIp;
    /**
     * 登陆系统(windows/linux/ios/android)
     */
    @TableField("login_os")
    private String loginOs;
    /**
     * 系统版本
     */
    @TableField("osver")
    private String osver;

    @TableField("creater")
    private String creater;

    @TableField("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @TableField("create_org")
    private String createOrg;

    @TableField("last_update_by")
    private String lastUpdateBy;

    @TableField("last_update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;

}