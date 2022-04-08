package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织实体类
 *
 * @author 李骐光
 * @date 2020-06-19
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_sys_organization_t")
public class IapSysOrganizationT implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 组织编码
     */
    @TableField("code")
    private String code;
    /**
     * 组织名称
     */
    @TableField("name")
    private String name;
    /**
     * 父级组织
     */
    @TableField("parent_org_id")
    private String parentOrgId;
    /**
     * 组织地址
     */
    @TableField("address")
    private String address;
    /**
     * 组织类别(1公司,2部门,3销售大区,4区域)
     */
    @TableField("type")
    private Short type;
    /**
     * 组织描述
     */
    @TableField("description")
    private String description;
    /**
     * 是否删除(0:已删除,1:未删除)
     */
    @TableField("delete_status")
    private Short deleteStatus;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 最后更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;

}
