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
@TableName("me_sfc_wip_log")
@ApiModel(value="SfcWiplog",description="生产过程日志表")
public class SfcWiplog extends Model<SfcWiplog> {
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

    @ApiModelProperty(value="排程【UK】")
    @TableField("SCHEDULE_BO")
    private String scheduleBo;

    @ApiModelProperty(value="工单")
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value="车间")
    @TableField("WORK_SHOP_BO")
    private String workShopBo;

    @ApiModelProperty(value="产线")
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;

    @ApiModelProperty(value="工序")
    @TableField("OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value="工位")
    @TableField("STATION_BO")
    private String stationBo;

    @ApiModelProperty(value="设备")
    @TableField("DEVICE_BO")
    private String deviceBo;

    @ApiModelProperty(value="工号")
    @TableField("USER_BO")
    private String userBo;

    @ApiModelProperty(value="班次")
    @TableField("SHIT_BO")
    private String shitBo;

    @ApiModelProperty(value="班组")
    @TableField("TEAM_BO")
    private String teamBo;

    @ApiModelProperty(value="物料")
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="物料清单")
    @TableField("BOM_BO")
    private String bomBo;

    @ApiModelProperty(value="sfc工艺路线")
    @TableField("SFC_ROUTER_BO")
    private String sfcRouterBo;

    @ApiModelProperty(value="sfc工序步骤")
    @TableField("SFC_STEP_ID")
    private String sfcStepId;

    @ApiModelProperty(value="父sfc")
    @TableField("PARENT_SFC_BO")
    private String parentSfcBo;

    @ApiModelProperty(value="状态")
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value="进站时间")
    @TableField("IN_TIME")
    private Date inTime;

    @ApiModelProperty(value="出站时间")
    @TableField("OUT_TIME")
    private Date outTime;

    @ApiModelProperty(value="是否批次过站(false-否,true-是)")
    @TableField("IS_BATCH")
    private boolean isBatch;

    @ApiModelProperty(value="生产批次")
    @TableField("PROCESS_LOT")
    private boolean processLot;

    @ApiModelProperty(value="SFC数量")
    @TableField("SFC_QTY")
    private BigDecimal sfcQty;

    @ApiModelProperty(value="进站数量")
    @TableField("INPUT_QTY")
    private BigDecimal inputQty;

    @ApiModelProperty(value="良品数量")
    @TableField("DONE_QTY")
    private BigDecimal doneQty;

    @ApiModelProperty(value="报废数量")
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty;

    @ApiModelProperty(value="是否重复加工")
    @TableField("IS_REWORK")
    private String isRework;

    @ApiModelProperty(value="处理结果 OK： 合格过站 NG：不合格过站")
    @TableField("RESULT")
    private String result;

    @ApiModelProperty(value="记录时间")
    @TableField("CREATE_DATE")
    private Date create_date;

    @ApiModelProperty(value="不良数量")
    @TableField("NC_QTY")
    private BigDecimal ncQty;

    @ApiModelProperty(value="工序工单")
    @TableField("OPERATION_ORDER")
    private String operationOrder;

    @ApiModelProperty(value="暂停时间")
    @TableField("STOP_TIME")
    private Date stopTime;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
