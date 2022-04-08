package com.itl.iap.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (IapSysUserT)实体类
 *
 * @author iAP
 * @since 2020-06-17 19:49:18
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_user_t")
public class SysUser extends BaseModel {
    private static final long serialVersionUID = 448002837646210912L;
    //0:正常,1：被锁定
    public static final short STATE_1 = 1;
    public static final short STATE_0 = 0;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 密码
     */
    @TableField("user_psw")
    private String userPsw;
    /**
     * 手机号码
     */
    @TableField("user_mobile")
    private String userMobile;
    /**
     * 用户编码/工号
     */
    @TableField("user_code")
    private String userCode;
    /**
     * 用户姓名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 用户直属领导
     */
    @TableField("parent_leader")
    private String parentLeader;
    /**
     * 身份证号码
     */
    @TableField("id_card_num")
    private String idCardNum;
    /**
     * 个人地址
     */
    @TableField("address")
    private String address;
    /**
     * 入职时间/注册时间
     */
    @TableField("register_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerDate;
    /**
     * 用户性别 0-female 1-male
     */
    @TableField("gender")
    private Short gender;
    /**
     * 用户生日
     */
    @TableField("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    /**
     * 用户个人签名
     */
    @TableField("signature")
    private String signature;
    /**
     * 用户设备push_token
     */
    @TableField("push_token")
    private String pushToken;
    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;
    /**
     * 传真
     */
    @TableField("fax")
    private String fax;
    /**
     * 用户手机号码
     */
    @TableField("mobile_phone")
    private String mobilePhone;
    /**
     * 用户办公电话
     */
    @TableField("office_phone")
    private String officePhone;
    /**
     * 状态 用户状态：0:正常状态,1：用户被锁定
     */
    @TableField("state")
    private Short state;
    /**
     * 户类型：0:内部用户 1:外部用户
     */
    @TableField("user_type")
    private Short userType;
    /**
     * 密码修改时间
     */
    @TableField("pwd_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pwdUpdateTime;
    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;
    /**
     * 创建时间
     */
    @TableField("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    /**
     * 创建岗位
     */
    @TableField("create_org")
    private String createOrg;
    /**
     * 最后更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
     * 最后更新时间
     */
    @TableField("last_update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;
    /**
     * 用户有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("validity")
    private Date validity;

    /**
     * 盐值
     */
    @TableField("salt")
    private String salt;

    @TableField(exist = false)
    private List<String> buttonRoles;
    /**
     * 用户卡号
     */
    @TableField("user_card_number")
    private String userCardNumber;
}