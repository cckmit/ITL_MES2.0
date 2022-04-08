package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("me_sfc_repair")
@ApiModel(value="SfcRepair",description="维修表")
public class SfcRepair extends Model<SfcRepair> {
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

    @ApiModelProperty(value="SFC不合格记录")
    @TableField("NG_LOG_BO")
    private String ngLogBo;

    @ApiModelProperty(value="维修原因(字典维护)")
    @TableField("REPAIR_REASON")
    private String repairReason;

    @ApiModelProperty(value="维修方式(字典维护)")
    @TableField("REPAIR_METHOD")
    private String repairMethod;

    @ApiModelProperty(value="替换物料的BO(可能用不到-随后删除)")
    @TableField("REPLACE_ITEM_BO")
    private String replaceItemBo;

    @ApiModelProperty(value="责任单位")
    @TableField("DUTY_UNIT")
    private String dutyUnit;

    @ApiModelProperty(value="备注")
    @TableField("REMARK")
    private String remark;

    @ApiModelProperty(value="维修时间")
    @TableField("REPAIR_TIME")
    private Date repairTime;

    @ApiModelProperty(value="替换物料的SN")
    @TableField("REPLACE_ITEM_SN")
    private String replaceItemSn;

    @ApiModelProperty(value="报废数量")
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty;

    @ApiModelProperty(value="报废原因")
    @TableField("NC_CODE_BO")
    private String ncCodeBo;

    @ApiModelProperty(value="返修OK数量，取表me_sfc_repair字段")
    @TableField("REPAIR_QTY")
    private BigDecimal repairQty;

    @ApiModelProperty(value="DUTY_OPERATION\t责任工序，取表me_sfc_repair字段")
    @TableField("DUTY_OPERATION")
    private String dutyOperation;

    @ApiModelProperty(value="责任人，取表me_sfc_repair字段")
    @TableField("DUTY_USER")
    private String dutyUser;

    @ApiModelProperty(value="报废工位")
    @TableField("STATION_BO")
    private String stationBo;

    @ApiModelProperty(value="是否是工步报工记录的报废，默认为0,1代表是")
    @TableField("STEP_REPAIR_FLAG")
    private String stepRepairFlag;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
