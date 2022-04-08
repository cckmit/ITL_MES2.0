package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.AssertTrue;
import java.util.Date;
import java.io.Serializable;

/**
 * 岗位组织中间表
 *
 * @author 马家伦
 * @date 2020-06-23
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_sys_organization_position_t")
@Accessors(chain = true)
public class IapSysOrganizationPositionT implements Serializable {
    private static final long serialVersionUID = 217414395949899659L;
    /**
    * 主键
    */
    private String id;
    /**
    * 组织id
    */
    @TableField("organization_id")
    private String organizationId;
    /**
    * 岗位id
    */
    @TableField("position_id")
    private String positionId;
    /**
    * 排序
    */
    private Short sort;
    /**
    * 状态：0:正常状态，1:被锁定
    */
    private Short state;
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
    * 0：正常，1：删除
    */
    @TableField("del_flag")
    private Short delFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;

}