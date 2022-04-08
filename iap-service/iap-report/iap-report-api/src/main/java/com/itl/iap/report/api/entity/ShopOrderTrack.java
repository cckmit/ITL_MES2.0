package com.itl.iap.report.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("m_shop_order")
public class ShopOrderTrack {

    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @TableField("SHOP_ORDER")
    @ApiModelProperty("工单编码")
    private String shopOrder;

    @TableField("ORDER_QTY")
    @ApiModelProperty("工单数量")
    private int orderQty;

    @TableField("PLAN_START_DATE")
    @ApiModelProperty("计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date planStartDate;

    @TableField("PLAN_END_DATE")
    @ApiModelProperty("计划完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date planEndDate;

    @TableField(exist = false)
    @ApiModelProperty("订单号")
    private String customerOrder;

    @TableField(exist = false)
    @ApiModelProperty("物料编码")
    private String item;

    @TableField(exist = false)
    @ApiModelProperty("物料名称")
    private String itemName;

    @TableField(exist = false)
    @ApiModelProperty("物料规格")
    private String itemDesc;

    @TableField(exist = false)
    @ApiModelProperty("批次条码")
    private String sfc;

    @TableField(exist = false)
    @ApiModelProperty("批次条码状态")
    private String state;

    @TableField(exist = false)
    @ApiModelProperty("批次条码数量")
    private int sfcQty;

    @TableField(exist = false)
    @ApiModelProperty("开始时间")
    private String startDate;

    @TableField(exist = false)
    @ApiModelProperty("完成时间")
    private String endDate;

    @TableField(exist = false)
    @ApiModelProperty("当前工序")
    private String operationBo;

    @TableField(exist = false)
    @ApiModelProperty("进站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date inTime;

    @TableField(exist = false)
    @ApiModelProperty("进站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @TableField(exist = false)
    @ApiModelProperty("工序名称")
    private String operationName;

    @TableField(exist = false)
    @ApiModelProperty("派工单编码")
    private String dispatchCode;

}
