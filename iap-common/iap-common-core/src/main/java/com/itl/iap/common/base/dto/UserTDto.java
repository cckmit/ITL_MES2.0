package com.itl.iap.common.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (IapSysUserT)DTO类
 *
 * @author iAP
 * @since 2020-06-19 15:08:09
 */
public class UserTDto implements Serializable {

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
     * 用户性别 0-female 1-male
     */
    private Short gender;

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
     * 创建人
     */
    private String creater;
    /**
     * 创建岗位
     */
    private String createOrg;
    /**
     * 最后更新人
     */
    private String lastUpdateBy;
    /**
     * 员工编号
     */
    private String employeeCode;
    /**
     * 部门ID
     */
    private String organizationId;
    /**
     * 角色
     */
    private String roleNames;
    /**
     * 员工类型
     */
    private String employeeType;
    /**
     * 客户端Id
     */
    private String clientId;

    /**
     * 内部 - 经销商
     */
    private Short type;

    /**
     * 人员产线
     */
    private String productLineBo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPsw() {
        return userPsw;
    }

    public void setUserPsw(String userPsw) {
        this.userPsw = userPsw;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentLeader() {
        return parentLeader;
    }

    public void setParentLeader(String parentLeader) {
        this.parentLeader = parentLeader;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreateOrg() {
        return createOrg;
    }

    public void setCreateOrg(String createOrg) {
        this.createOrg = createOrg;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getProductLineBo() {
        return productLineBo;
    }

    public void setProductLineBo(String productLineBo) {
        this.productLineBo = productLineBo;
    }
}
