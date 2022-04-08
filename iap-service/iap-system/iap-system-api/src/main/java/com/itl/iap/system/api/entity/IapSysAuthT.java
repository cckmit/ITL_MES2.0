package com.itl.iap.system.api.entity;

import java.util.Date;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 权限表实体类
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_auth_t")
public class IapSysAuthT extends BaseModel {
    private static final long serialVersionUID = -49902977697983515L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 父id
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 权限名字
     */
    @TableField("auth_name")
    private String authName;
    /**
     * 权限编码
     */
    @TableField("auth_code")
    private String authCode;
    /**
     * 权限类型：0:内部权限，1:外部权限
     */
    @TableField("auth_type")
    private Short authType;
    /**
     * 权限描述
     */
    @TableField("description")
    private String description;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;
    /**
     * 0：正常，1：删除
     */
    @TableField("del_flag")
    private Short delFlag;

}