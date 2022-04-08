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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("me_sfc_nc_log")
@ApiModel(value="SfcNcLog",description="Sfc不合格记录表")
public class SfcNcLog extends Model<SfcNcLog> {

    private static final long serialVersionUID = 5248229075574929653L;

    @ApiModelProperty(value="唯一标识【PK】")
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂")
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="SN【FK】成品")
    @TableField("SFC")
    private String sfc;

    @ApiModelProperty(value="SFC过程")
    @TableField("WIP_LOG_BO")
    private String wipLogBo;

    @ApiModelProperty(value="是否原材料检测")
    @TableField("IS_RAW_CHECK")
    private String isRawCheck;

    @ApiModelProperty(value="组件BO")
    @TableField("COMPONENT_BO")
    private String componentBo;

    @ApiModelProperty(value="不合格代码BO")
    @TableField("NC_CODE_BO")
    private String ncCodeBo;

    @ApiModelProperty(value="问题产线")
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;

    @ApiModelProperty(value="问题工序")
    @TableField("OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value="问题工位")
    @TableField("STATION_BO")
    private String stationBo;

    @ApiModelProperty(value="问题设备")
    @TableField("DEVICE_BO")
    private String deviceBo;

    @ApiModelProperty(value="问题工号")
    @TableField("USER_BO")
    private String userBo;

    @ApiModelProperty(value="备注")
    @TableField("REMARK")
    private String remark;

    @ApiModelProperty(value="记录时间")
    @TableField("RECORD_TIME")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date recordTime;

    @ApiModelProperty(value="不良数量")
    @TableField("NC_QTY")
    private BigDecimal ncQty;

    @ApiModelProperty(value="工序在m_router_process中的流程id,如果有多个用 | 符号隔开")
    @TableField("OP_IDS")
    private String opIds;

    @ApiModelProperty(value="是否返修完成（0:未返修 1:返修过）")
    @TableField("IS_REPAIRED")
    private String isRepaired;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
