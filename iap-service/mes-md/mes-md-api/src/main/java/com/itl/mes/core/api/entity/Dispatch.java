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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@TableName("m_dispatch")
@ApiModel(value="Dispatch",description="工单工序任务派工表")
@Data
public class Dispatch extends Model<Dispatch> {

    @ApiModelProperty(value="UUID")
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value="工序工单号")
    @TableField("OPERATION_ORDER")
    private String operationOrder;

    @ApiModelProperty(value="工序")
    @TableField("OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value="工序工单数量（工单下达的数量）")
    @TableField("OPERATION_ORDER_QTY")
    private BigDecimal operationOrderQty;

    @ApiModelProperty(value="设备编码")
    @TableField("DEVICE")
    private String device;

    @ApiModelProperty(value="派工数量")
    @TableField("DISPATCH_QTY")
    private BigDecimal dispatchQty;

    @ApiModelProperty(value="派工指定人员")
    @TableField("DISPATCH_OP")
    private String dispatchOp;

    @ApiModelProperty(value="物料")
    @TableField("ITEM")
    private String item;

    @ApiModelProperty(value="待生产WIP，每个批次条码SFC进站、出站时更新一次数量（此数量也为可改派数量）")
    @TableField("WAIT_IN")
    private BigDecimal waitIn;

    @ApiModelProperty(value="创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="创建时间")
    @TableField("CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @TableField(exist = false)
    private String itemName;

    @TableField(exist = false)
    private String itemDesc;

    @TableField(exist = false)
    @ApiModelProperty("图号")
    private String drawingNo;

    @TableField(exist = false)
    private String operation;

    @TableField(exist = false)
    private String isUrgent;

    @ApiModelProperty(value="指定人员")
    @TableField(exist = false)
    private String dispatchOpName;

    @ApiModelProperty(value="设备名称")
    @TableField(exist = false)
    private String deviceName;

    @ApiModelProperty(value="设备描述")
    @TableField(exist = false)
    private String deviceDesc;

    @ApiModelProperty(value="批次条码")
    @TableField(exist = false)
    private String sfc;

    @ApiModelProperty(value="是否需要派工到设备，1:不需要 0:需要")
    @TableField("IS_NEED_DISPATCH")
    private String isNeedDispatch;

    @ApiModelProperty(value="未完成数量，当这个数量为0时，说明该工序工单在此工序已完成")
    @TableField("NOT_DONE_QTY")
    private BigDecimal notDoneQty;

    @ApiModelProperty(value = "工序名称")
    @TableField(exist = false)
    private String operationName;

    @ApiModelProperty(value = "工单BO")
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value = "物料BO")
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value = "线圈是否进站完成，0:未完成 1:已完成")
    @TableField("IS_STEM_INPUT_FINISHED")
    private String isStemInputFinished;

    @ApiModelProperty(value = "派工单编码（唯一）（IS_NEED_DISPATCH字段为0的不记录编码）")
    @TableField("DISPATCH_CODE")
    private String dispatchCode;

    @ApiModelProperty(value = "是否是首工序（0:不是 1:是）")
    @TableField("IS_FIRST_OPERATION")
    private String isFirstOperation;

    @ApiModelProperty(value = "可打印数量（默认为派工数量，随着SFC的打印减少直到0）")
    @TableField("CAN_PRINT_QTY")
    private BigDecimal canPrintQty;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

    @TableField(exist = false)
    private List<String> sfcList;

    @TableField(exist = false)
    private List<Map<String,String>> bomMapList;

    @TableField("IS_DISPATCH_FINISHED")
    private String isDispatchFinished;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
