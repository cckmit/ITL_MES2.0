package com.itl.iap.report.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("me_sfc_repair")
public class SfcScrap {
    @TableId(value = "BO", type = IdType.INPUT)
    @ApiModelProperty("主键id")
    private String BO;

    @TableField("SFC")
    @ApiModelProperty("批次条码")
    private String sfc;

    @TableField("SCRAP_QTY")
    @ApiModelProperty("报废数量")
    private BigDecimal scrapQty;

    @TableField("NC_CODE_BO")
    @ApiModelProperty("报废原因BO")
    private String ncCodeBo;

    @TableField("DUTY_OPERATION")
    @ApiModelProperty("报废工序BO")
    private String dutyOperation;

    @TableField("DUTY_USER")
    @ApiModelProperty("报废人员")
    private String dutyUser;

    @TableField("REPAIR_TIME")
    @ApiModelProperty("报废时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date repairTime;

    @TableField("STATION_BO")
    @ApiModelProperty("工位BO")
    private String stationBo;

    @TableField(exist = false)
    @ApiModelProperty("工单")
    private String shopOrder;

    @TableField(exist = false)
    @ApiModelProperty("工序工单")
    private String operationOrder;

    @TableField(exist = false)
    @ApiModelProperty("物料编码")
    private String item;

    @TableField(exist = false)
    @ApiModelProperty("物料名称")
    private String itemName;

    @TableField(exist = false)
    @ApiModelProperty("报废工序名称")
    private String operationName;

    @TableField(exist = false)
    @ApiModelProperty("报废人员名称")
    private String dutyUserName;

    @TableField(exist = false)
    @ApiModelProperty("报废原因名称")
    private String ncName;

    @TableField(exist = false)
    @ApiModelProperty("报废工位名称")
    private String stationName;

    @TableField(exist = false)
    @ApiModelProperty("退料同步状态")
    private String erpSuccessFlag;

    @TableField(exist = false)
    @ApiModelProperty("失败原因")
    private String erpFailedReason;

}
