package com.itl.iap.system.api.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 用户dto
 *
 * @author 谭强
 * @date 2020-06-19
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapSysUserTDto implements Serializable {
    private static final long serialVersionUID = -22245589193251740L;
    //分页对象
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerDate;
    /**
     * 用户性别 0-female 1-male
     */
    private Short gender;
    /**
     * 用户生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
     * 户类型：0:内部用户 1:外部用户
     */
    private Short userType;

    /**
     * 密码修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pwdUpdateTime;
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
    /**
     * 员工编号
     */
    private String employeeCode;
    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;
    /**
     * 关联角色
     */
    private List<IapSysRoleTDto> iapSysRoleTDtoList;
    /**
     * 关联管理
     */
    private List<IapPositionTDto> positionTDtoList;
    /**
     * 所属部门
     */
    private List<IapSysOrganizationT> iapSysOrganizationT;
    /**
     * 部门ID
     */
    private String organizationId;
    /**
     * 部门ID集合
     */
    private List<String> organizations;

    /**
     * 角色
     */
    private String roleNames;
    /**
     * 员工类型
     */
    private String employeeType;

    /**
     * 内部 - 经销商
     */
    private Short type;

    /**
     * 用户有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validity;

    private IapEmployeeTDto employeeDto;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 聊天功能，群主（0-群成员，1-群主）
     */
    private String host;

    /**
     * 岗位id
     */
    private String positionId;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 派工系数
     */
    private BigDecimal dispatchRatio;

    /**
     * 用户卡号
     */
    private String userCardNumber;

    /**
     * 车间bo
     */
    private String workShopBo;

    /**
     *车间名称
     */
    private String workShopName;

    /**
     * 所属车间bo
     */
    private String belongWorkShopBo;

    /**
     *所属车间名称
     */
    private String belongWorkShopName;
    /**
     * 内部工号
     */
    private String insideNo;
}
