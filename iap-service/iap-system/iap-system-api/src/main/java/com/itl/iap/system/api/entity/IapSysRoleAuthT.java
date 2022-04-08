package com.itl.iap.system.api.entity;

import java.util.Date;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色权限中间表实体类
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_role_auth_t")
public class IapSysRoleAuthT extends BaseModel {
    private static final long serialVersionUID = 717363565991555276L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 角色id
     */
    @TableField("role_id")
    private String roleId;
    /**
     * 权限id
     */
    @TableField("auth_id")
    private String authId;
    /**
     * 排序
     */
    @TableField("sort")
    private Short sort;
    /**
     * 状态：0:正常状态，1:被锁定
     */
    @TableField("state")
    private Short state;
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
     * 最后更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
     * 最后更新时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;
    /**
     * 0：正常，1：删除
     */
    @TableField("del_flag")
    private Short delFlag;

}