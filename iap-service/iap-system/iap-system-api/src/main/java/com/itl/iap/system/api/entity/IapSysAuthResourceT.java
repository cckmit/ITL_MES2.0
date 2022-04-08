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
 * 用户菜单实体类
 *
 * @author 谭强
 * @date 2020-06-22
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_auth_resource_t")
public class IapSysAuthResourceT extends BaseModel {
    private static final long serialVersionUID = 650020975083682510L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 资源菜单id
     */
    @TableField("resource_id")
    private String resourceId;
    /**
     * 权限id
     */
    @TableField("auth_id")
    private String authId;
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