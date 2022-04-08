package com.itl.mes.core.api.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("m_enter")
public class Enter {

    @ApiModelProperty(value="UUID")
    @TableId(value = "bo", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="设备bo")
    @TableField("DEVICE_BO")
    private String deviceBo;

    @ApiModelProperty(value="批次条码")
    @TableField("SFC")
    private String sfc;

    @ApiModelProperty(value="当前工序进站时间")
    @TableField("IN_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date inTime;

    @ApiModelProperty(value="工序bo")
    @TableField("OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value="0 SFC进站 1 首工序进站")
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value="工单")
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value="工序工单")
    @TableField("OPERATION_ORDER")
    private String operationOrder;

    @ApiModelProperty(value="进站人")
    @TableField("user_bo")
    private String userBo;

}
