package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工实体类
 *
 * @author 马家伦
 * @date 2020-06-17
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iap_sys_employee_t")
public class IapEmployeeT implements Serializable {
    private static final long serialVersionUID = -16162767516446731L;
    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 员工编码
     */
    private String code;
    /**
     * 联系方式
     */
    private String contract;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态(0-离职，1-在职)
     */
    private Short status;
    /**
     * 0-经销商，1-内部
     */
    private Short type;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;
    /**
     * 创建者组织
     */
    @TableField("create_org")
    private String createOrg;
    /**
     * 最后修改人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;
    /**
     * 备注
     */
    private String remark;

    /**
     * 分配的用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 是否开户(0-否，1-是)
     */
    @TableField("open_account")
    private Short openAccount;

}