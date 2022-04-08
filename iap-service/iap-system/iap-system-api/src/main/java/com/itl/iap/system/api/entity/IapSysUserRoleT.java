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
 * 用户角色实体类
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_user_role_t")
public class IapSysUserRoleT extends BaseModel {
    private static final long serialVersionUID = 172696013684450394L;

    @TableId
    private String id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 权限id
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 创建者
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
     * 最后修改人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
     * 最后修改时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

}