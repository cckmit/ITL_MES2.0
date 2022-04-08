package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 岗位实体类
 *
 * @author 马家伦
 * @date 2020-06-16
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iap_sys_position_t")
public class IapPositionT implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 职位编码
     */
    private String code;

    /**
     * 职位类型
     */
    private Short type;

    /**
     * 职位排序
     */
    private Short sort;
    /**
     *  状态(0-正常状态，1-被锁定)
     */
    private Short enabled;

    /**
     * 上级职位
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 主要负责人
     */
    @TableField("main_leader_id")
    private String mainLeaderId;

    /**
     * 创建组织
     */
    @TableField("create_org")
    private String createOrg;

    /**
     * 修改人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 备注
     */
    private String remark;

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
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;
}
