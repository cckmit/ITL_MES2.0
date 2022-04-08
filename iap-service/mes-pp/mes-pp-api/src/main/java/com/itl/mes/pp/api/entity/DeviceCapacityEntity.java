package com.itl.mes.pp.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 机台产能表
 *
 * @author cuichonghe
 * @date 2020-12-18 14:05:47
 */
@Data
@TableName("p_device_capacity")
public class DeviceCapacityEntity implements Serializable  {
    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    @TableId
    @ApiModelProperty(value = "bo")
    private String bo;
    /**
     * 工厂【UK】
     */
    @ApiModelProperty(value = "工厂【UK】")
    private String site;
    @ApiModelProperty(value = "车间")
    @TableField(exist = false)
    private String workShop;
    /**
     * 产线【UK】
     */
    @ApiModelProperty(value = "产线【UK】")
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;
    /**
     * 机台
     */
    @ApiModelProperty(value = "机台")
    @TableField("DEVICE_BO")
    private String deviceBo;
    /**
     * 所属车间 BO【UK】
     */
    @ApiModelProperty(value = "所属车间 BO【UK】")
    @TableField("WORK_SHOP_BO")
    private String workShopBo;


    /**
     * 建档日期
     */
    @ApiModelProperty(value = "建档日期")
    @TableField("CREATE_DATE")
    private Date createDate;
    /**
     * 建档人
     */
    @ApiModelProperty(value = "建档人")
    @TableField("CREATE_USER")
    private String createUser;
    /**
     * 修改日期
     */
    @ApiModelProperty(value = "修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;
    /**
     * 额定保养时间
     */
    @ApiModelProperty(value = "额定保养时间")
    @TableField("MAINTENANCE_TIME")
    private Integer maintenanceTime;
    /**
     * 产能利用率
     */
    @ApiModelProperty(value = "产能利用率")
    private String percentage;
    /**
     * 额定开机时间
     */
    @ApiModelProperty(value = "额定开机时间")
    @TableField("RATED_TIME")
    private Integer ratedTime;

    /**
     * 最大开机时间
     */
    @ApiModelProperty(value = "最大开机时间")
    @TableField("MAX_TIME")
    private Integer maxTime;

    @ApiModelProperty(value = "产线DESC")
    @TableField(exist = false)
    private String productLineDesc;


}
