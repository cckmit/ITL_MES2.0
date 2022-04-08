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
 * 岗位用户中间表
 *
 * @author 谭强
 * @date 2020-06-24
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_position_user_t")
public class IapSysPositionUserT extends BaseModel {
    private static final long serialVersionUID = 282317459896533736L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 用户
     */
    @TableField("user_id")
    private String userId;
    /**
     * 岗位
     */
    @TableField("position_id")
    private String positionId;
    /**
     * 创建者
     */
    private String creater;
    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;
    /**
     * 创建组织
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