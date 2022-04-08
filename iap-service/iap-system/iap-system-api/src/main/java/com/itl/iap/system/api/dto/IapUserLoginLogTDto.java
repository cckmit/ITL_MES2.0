package com.itl.iap.system.api.dto;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户登录日志dto
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapUserLoginLogTDto implements Serializable {
    private static final long serialVersionUID = -86030013071014276L;
    // 0.第三方 1. 邮箱 2.手机 3.密码
    public static final Short LOGIN_TYPE0 = 0;
    public static final Short LOGIN_TYPE1 = 1;
    public static final Short LOGIN_TYPE2 = 2;
    public static final Short LOGIN_TYPE3 = 3;
    // 操作类型 1登陆成功  2登出成功 3登录失败 4登出失败
    public static final Short LOGIN_COMMAND1 = 1;
    public static final Short LOGIN_COMMAND2 = 2;
    public static final Short LOGIN_COMMAND3 = 3;
    public static final Short LOGIN_COMMAND4 = 4;
    //分页对象
    private Page page;

    private String id;
    private String userId;
    private String userName;
    /**
     * 登陆方式(登录方式 第三方/邮箱/手机等)
     */
    private Short loginType;
    /**
     * 操作类型 1登陆成功  2登出成功 3登录失败 4登出失败
     */
    private Short command;
    private String version;
    private String client;
    private String deviceId;
    private String lastIp;
    /**
     * 登陆系统(windows/linux/ios/android)
     */
    private String loginOs;
    /**
     * 系统版本
     */
    private String osver;
    private String creater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    private String createOrg;
    private String lastUpdateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;

    private Date createDateStart;
    private Date createDateEnd;
    private String message;
}