package com.itl.iap.report.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

@Data
@TableName("me_sfc_wip_log")
@ApiModel(value="SfcWipLog",description="过站报表")
public class SfcWipLog extends Model<SfcWipLog> {

    private static final long serialVersionUID = 4799971501987859421L;


    @ApiModelProperty(value="序号【PK】")
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    @TableField(value = "SITE")
    private String site;

    @ApiModelProperty(value="SFC编号【FK】")
    @TableField(value = "SFC")
    private String sfc;

    @ApiModelProperty(value="排程【UK】")
    @TableField(value = "SCHEDULE_BO")
    private String scheduleBo;

    @ApiModelProperty(value="工单")
    @TableField(value = "SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value="车间")
    @TableField(value = "WORK_SHOP_BO")
    private String workShopBo;

    @ApiModelProperty(value="产线")
    @TableField(value = "PRODUCT_LINE_BO")
    private String productLineBo;

    @ApiModelProperty(value="工序")
    @TableField(value = "OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value="工位")
    @TableField(value = "STATION_BO")
    private String stationBo;

    @ApiModelProperty(value="设备")
    @TableField(value = "DEVICE_BO")
    private String deviceBo;

    @ApiModelProperty(value="工号")
    @TableField(value = "USER_BO")
    private String userBo;

    @ApiModelProperty(value="班次")
    @TableField(value = "SHIT_BO")
    private String shitBo;

    @ApiModelProperty(value="班组")
    @TableField(value = "TEAM_BO")
    private String teamBo;

    @ApiModelProperty(value="物料")
    @TableField(value = "ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="物料清单")
    @TableField(value = "BOM_BO")
    private String bomBo;

    @ApiModelProperty(value="SFC工艺路线")
    @TableField(value = "SFC_ROUTER_BO")
    private String sfcRouterBo;

    @ApiModelProperty(value="SFC工序步骤")
    @TableField(value = "SFC_STEP_ID")
    private String sfcStepId;

    @ApiModelProperty(value="父SFC")
    @TableField(value = "PARENT_SFC_BO")
    private String parentSfcBo;

    @ApiModelProperty(value="状态")
    @TableField(value = "STATE")
    private String state;

    @ApiModelProperty(value="进站时间")
    @TableField(value = "IN_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inTime;

    @ApiModelProperty(value="出站时间")
    @TableField(value = "OUT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outTime;

    @ApiModelProperty(value="是否批次过站(false-否,true-是)")
    @TableField(value = "IS_BATCH")
    private String isBatch;

    @ApiModelProperty(value="生产批次")
    @TableField(value = "PROCESS_LOT")
    private String processLot;

    @ApiModelProperty(value="SFC数量")
    @TableField(value = "SFC_QTY")
    private String sfcQty;

    @ApiModelProperty(value="SFC数量投入数量")
    @TableField(value = "INPUT_QTY")
    private String inputQty;

    @ApiModelProperty(value="SFC数量良品数量")
    @TableField(value = "DONE_QTY")
    private String doneQty;

    @ApiModelProperty(value="报废数量")
    @TableField(value = "SCRAP_QTY")
    private String scrapQty;

    @ApiModelProperty(value="是否是重复加工")
    @TableField(value = "IS_REWORK")
    private String isRework;

    @ApiModelProperty(value="处理结果 OK： 合格过站 NG：不合格过站")
    @TableField(value = "RESULT")
    private String result;

    @ApiModelProperty(value="记录时间")
    @TableField(value = "CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value="不良数量")
    @TableField(value = "NC_QTY")
    private String ncQty;

    @ApiModelProperty(value="工序工单")
    @TableField(value = "OPERATION_ORDER")
    private String operationOrder;

    @ApiModelProperty(value="暂停时间")
    @TableField(value = "STOP_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date stopTime;

    @ApiModelProperty(value="工单")
    @TableField(exist = false)
    private String shopOrder;

    @ApiModelProperty(value="工序名称")
    @TableField(exist = false)
    private String operationName;

    @ApiModelProperty(value="物料编码")
    @TableField(exist = false)
    private String item;

    @ApiModelProperty(value="物料名称")
    @TableField(exist = false)
    private String itemName;

    @ApiModelProperty(value="设备名称")
    @TableField(exist = false)
    private String deviceName;

    @ApiModelProperty(value="设备编码")
    @TableField(exist = false)
    private String device;

    @ApiModelProperty(value="人员")
    @TableField(exist = false)
    private String realName;

    @ApiModelProperty(value="物料描述")
    @TableField(exist = false)
    private String itemDesc;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
