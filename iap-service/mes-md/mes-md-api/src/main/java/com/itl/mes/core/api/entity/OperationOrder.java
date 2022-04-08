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
import java.util.Date;

@Data
@TableName("m_operation_order")
@ApiModel(value="OperationOrder",description="工单工序任务表")
public class OperationOrder extends Model<OperationOrder> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @TableField("OPERATION_ORDER")
    private String operationOrder;

    @TableField("SHOP_ORDER")
    private String shopOrder;

    @TableField("OPERATION_ORDER_STATE")
    private String operationOrderState;

    @TableField("OPERATION_ORDER_QTY")
    private BigDecimal operationOrderQty;

    @TableField("ROUTRE")
    private String routre;

    @TableField("VERSION")
    private String version;

    @TableField("ITEM")
    private String item;

    @TableField("WORKSHOP")
    private String workShop;

    @TableField("ITEM_NAME")
    private String itemName;

    @TableField("ITER_VERSION")
    private String iterVersion;

    @TableField("IS_URGENT")
    private String isUrgent;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @TableField(exist =  false)
    private String itemDesc;

    @TableField(exist = false)
    private String operation;

    @TableField(exist = false)
    private String operationBo;

    @TableField(exist = false)
    private String operationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "可派工数量")
    private BigDecimal canDispatchQty;

    @TableField("IS_STEM_INPUT_FINISHED")
    private String isStemInputFinished;

    public final static String INPUT_FINISHED_SUCCESS_FLAG = "1";
    
    @Override
    protected Serializable pkVal() {
        return this.bo;
    }
}
