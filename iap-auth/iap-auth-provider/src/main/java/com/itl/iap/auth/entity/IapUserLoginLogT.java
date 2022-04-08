package com.itl.iap.auth.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (IapUserLoginLogT)实体类
 *
 * @author iAP
 * @since 2020-06-29 13:30:13
 */
@Data
@Accessors(chain = true)
@TableName("iap_user_login_log_t")
public class IapUserLoginLogT extends BaseModel {
    private static final long serialVersionUID = -16668989776531428L;

    /**
     * 登录类型
     * 0.第三方 1. 邮箱 2.手机 3.密码 4.刷卡
     */
    public static final short LOGIN_TYPE0 = 0;
    public static final short LOGIN_TYPE1 = 1;
    public static final short LOGIN_TYPE2 = 2;
    public static final short LOGIN_TYPE3 = 3;
    public static final short LOGIN_TYPE4 = 4;

    /**
     * 操作类型
     * 1登陆成功  2登出成功 3登录失败 4登出失败
     */
    public static final short LOGIN_COMMAND1 = 1;
    public static final short LOGIN_COMMAND2 = 2;
    public static final short LOGIN_COMMAND3 = 3;
    public static final short LOGIN_COMMAND4 = 4;

    /**
     * 主键ID
      */
    @TableId
    @TableField("id")
    private String id;

    /**
     * 用户ID
     */
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

    /**
     * 版本
     */
    @TableField("version")
    private String version;

    /**
     * 客户端
     */
    @TableField("client")
    private String client;

    /**
     * 设备ID
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * ip 地址
     */
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

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 创建岗位
     */
    @TableField("create_org")
    private String createOrg;

    /**
     * 更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 最后更新时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

    /**
     * 登录信息
     */
    @TableField("message")
    private String message;
}