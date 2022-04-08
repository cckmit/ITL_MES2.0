package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("m_dy_step_dispatch")
@ApiModel(value="StemDispatch",description="线圈工步派工表")
public class StemDispatch extends Model<StemDispatch> {

    /**
     * uuid，唯一标识
     */
    @ApiModelProperty(value="唯一标识")
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;
    /**
     * 派工单编码，自动生成：DIS+YYYY
     */
    @ApiModelProperty(value="派工单编码，自动生成：DIS+YYYY")
    @TableField("STEP_DISPATCH_CODE")
    private String stepDispatchCode;
    /**
     * 工步编码
     */
    @ApiModelProperty(value="工步编码")
    @TableField("WORK_STEP_CODE")
    private String workStepCode;
    /**
     * 工步名称
     */
    @ApiModelProperty(value="工步名称")
    @TableField("WORK_STEP_NAME")
    private String workStepName;
//    /**
//     * 设备编码
//     */
//    @ApiModelProperty(value="设备编码")
//    @TableField("DEVICE")
//    private String device;
//    /**
//     * 设备名称
//     */
//    @ApiModelProperty(value="设备名称")
//    @TableField("DEVICE_NAME")
//    private String deviceName;
    /**
     * 工序工单号
     */
    @ApiModelProperty(value="工序工单号")
    @TableField("OPERATION_ORDER")
    private String operationOrder;
    /**
     * 物料编码
     */
    @ApiModelProperty(value="物料编码")
    @TableField("ITEM")
    private String item;
    /**
     * 物料名称
     */
    @ApiModelProperty(value="物料名称")
    @TableField("ITEM_NAME")
    private String itemName;
    /**
     * 物料描述
     */
    @ApiModelProperty(value="物料描述")
    @TableField("ITEM_DESC")
    private String itemDesc;
    /**
     * 派工数量
     */
    @ApiModelProperty(value="派工数量")
    @TableField("DISPATCH_QTY")
    private BigDecimal dispatchQty;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    @TableField("CREATE_USER")
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @TableField("CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value="用户名称")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value="用户编码")
    @TableField("USER_ID")
    private String userId;

    @ApiModelProperty(value="工单编码")
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value="用户组ID")
    @TableField("ROLE_ID")
    private String roleId;

    @ApiModelProperty(value="内部员工ID")
    @TableField("INSIDE_NO")
    private String insideNo;

    @ApiModelProperty(value="报工标识（0代表未完成报工 1代表完成报工）")
    @TableField("WORK_REPORT_FLAG")
    private String workReportFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
