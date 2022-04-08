package com.itl.iap.auth.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (IapSysUserT)DTO类
 *
 * @author iAP
 * @since 2020-06-17 19:49:19
 */
@Data
@Accessors(chain = true)
public class SysUserDto implements Serializable {
    private static final long serialVersionUID = -60852378266177541L;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * id
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPsw;

    /**
     * 手机号码
     */
    private String userMobile;

    /**
     * 用户编码/工号
     */
    private String userCode;

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户直属领导
     */
    private String parentLeader;

    /**
     * 身份证号码
     */
    private String idCardNum;

    /**
     * 个人地址
     */
    private String address;

    /**
     * 入职时间/注册时间
     */
    private Date registerDate;

    /**
     * 用户性别 0-female 1-male
     */
    private Short gender;

    /**
     * 用户生日
     */
    private Date birthday;

    /**
     * 用户个人签名
     */
    private String signature;

    /**
     * 用户设备push_token
     */
    private String pushToken;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 传真
     */
    private String fax;

    /**
     * 用户手机号码
     */
    private String mobilePhone;

    /**
     * 用户办公电话
     */
    private String officePhone;

    /**
     * 状态 用户状态：0:正常状态,1：用户被锁定
     */
    private Short state;

    /**
     * 用户类型：0:内部用户 1:外部用户
     */
    private Short userType;

    /**
     * 密码修改时间
     */
    private Date pwdUpdateTime;

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
     * 用户有效期
     */
    private Date validity;

    /**
     * 盐值
     */
    private String salt;
}