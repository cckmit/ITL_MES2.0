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
@TableName("d_dy_step_produce")
@ApiModel(value = "StepProduce",description = "线圈生产表")
public class StepProduce extends Model<StepProduce> {

    @ApiModelProperty(value="唯一标识")
    @TableId(value = "ID",type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "工单BO")
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value = "物料BO")
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value = "工序工单")
    @TableField("OPERATION_ORDER")
    private String operationOrder;

    @ApiModelProperty(value = "工序BO")
    @TableField("OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value = "状态为进站为进站数量，状态为出站为出站数量")
    @TableField("QTY")
    private BigDecimal qty;

    @ApiModelProperty(value = "状态 0:进站  1:出站")
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value = "操作人（当前登录人）")
    @TableField("USER_BO")
    private String userBo;

    @ApiModelProperty(value = "报工人（出站时记录）")
    @TableField("REPORT_WORK_USER")
    private String reportWorkUser;

    @ApiModelProperty(value = "创建时间（对应进出站时间）")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value = "线圈是否出站完成，0:未完成 1:已完成（此字段只针对state为0的数据有用）")
    @TableField("IS_STEM_OUTPUT_FINISHED")
    private String isStemOutputFinished;

//    @ApiModelProperty(value = "派工单编码")
//    @TableField("DISPATCH_CODE")
//    private String dispatchCode;

    public final static String INPUT_FLAG = "0";
    public final static String OUTPUT_FLAG = "1";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
