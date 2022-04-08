package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
import java.util.Date;

@Data
@TableName("m_dy_feedin")
@ApiModel(value="FeedIn",description="上料")
public class FeedIn extends Model<FeedIn> {

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }

    @ApiModelProperty(value="FEEDIN")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工单")
    @Length( max = 32 )
    @TableField("SHOP_ORDER_BO")
    @Excel( name="工单", orderNum="0" )
    private String shopOrderBo;

    @ApiModelProperty(value="工单")
    @TableField(exist = false)
    private String shopOrder;

    @ApiModelProperty(value="设备")
    @Length( max = 32 )
    @TableField("DEVICE_BO")
    @Excel( name="设备", orderNum="0" )
    private String deviceBo;

    @ApiModelProperty(value="上料SFC")
    @Length( max = 32 )
    @TableField("ASSY_SFC")
    @Excel( name="上料SFC", orderNum="0" )
    private String assySfc;

    @ApiModelProperty(value="上料数=表M_DY_STOCK的OK_QTY")
    @Length( max = 32 )
    @TableField("QTY")
    @Excel( name="上料数=表M_DY_STOCK的OK_QTY", orderNum="0" )
    private int qty;

    @ApiModelProperty(value="物料")
    @Length( max = 32 )
    @TableField("ITEM_BO")
    @Excel( name="物料", orderNum="0" )
    private String itemBo;

    @ApiModelProperty(value="物料编码")
    @TableField(exist = false)
    private String item;

    @ApiModelProperty(value="物料描述")
    @TableField(exist = false)
    private String itemDesc;

    @ApiModelProperty(value="上料人员")
    @Length( max = 32 )
    @TableField("FEEDING_USER")
    @Excel( name="上料人员", orderNum="0" )
    private String feedingUser;

    @ApiModelProperty(value="上料时间")
    @Length( max = 32 )
    @TableField("FEEDING_DATE")
    @Excel( name="上料时间", orderNum="0" )
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date feedingDate;

    @ApiModelProperty(value="状态")
    @Length( max = 32 )
    @TableField("STATE")
    @Excel( name="状态", orderNum="0" )
    private String state;

    @ApiModelProperty(value = "sfc剩余数量")
    @Length( max = 32 )
    @TableField("SURPLUS_QTY")
    @Excel( name="sfc剩余数量", orderNum="0" )
    private int surplusQty;

    @ApiModelProperty(value = "组件BO")
    @Length( max = 32 )
    @TableField("COMPONENT_BO")
    @Excel( name="组件BO", orderNum="0" )
    private String componentBo;
}
