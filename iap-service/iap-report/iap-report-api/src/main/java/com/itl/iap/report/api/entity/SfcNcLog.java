package com.itl.iap.report.api.entity;

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

@Data
@TableName("me_sfc_nc_log")
@ApiModel(value="SfcNcLog",description="Sfc不合格记录表")
public class SfcNcLog extends Model<SfcNcLog> {

    private static final long serialVersionUID = 4684351496403745004L;

    @ApiModelProperty(value="序号【PK】")
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂")
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="SN【FK】成品")
    @TableField("SFC")
    private String sfc;

    @ApiModelProperty(value="SN【FK】成品")
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
    private String recodeTime;

    @ApiModelProperty(value="不良数量")
    @TableField("NC_QTY")
    private String ncQty;

    @ApiModelProperty(value="工单")
    @TableField(exist = false)
    private String shopOrder;

    @ApiModelProperty(value="工序工单")
    @TableField(exist = false)
    private String operationOrder;

    @ApiModelProperty(value="物料编码")
    @TableField(exist = false)
    private String item;

    @ApiModelProperty(value="物料名称")
    @TableField(exist = false)
    private String itemName;

    @ApiModelProperty(value="工序名称")
    @TableField(exist = false)
    private String operationName;

    @ApiModelProperty(value="不合格代码名称")
    @TableField(exist = false)
    private String ncName;

    @ApiModelProperty(value="工位名称")
    @TableField(exist = false)
    private String stationName;

    @ApiModelProperty(value="人员")
    @TableField(exist = false)
    private String realName;

    @ApiModelProperty(value="返修OK数量")
    @TableField(exist = false)
    private String repairQty;

    @ApiModelProperty(value="报废数量")
    @TableField(exist = false)
    private String scrapQty;

    @ApiModelProperty(value="责任工序（工序名称）")
    @TableField(exist = false)
    private String dutyOperation;

    @ApiModelProperty(value="责任人(真实姓名)")
    @TableField(exist = false)
    private String dutyUser;

    @ApiModelProperty(value="状态")
    @TableField(exist = false)
    private String state;

    @ApiModelProperty(value="维修时间")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String repairTime;













    @Override
    protected Serializable pkVal() {
        return null;
    }
}
