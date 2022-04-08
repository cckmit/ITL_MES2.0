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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("d_dy_report_work")
@ApiModel(value="ReportWork",description="工步报工")
@Data
public class ReportWork extends Model<ReportWork> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @TableField("SFC")
    @Excel( name="SFC", orderNum="1" )
    private String sfc;

    @TableField("ITEM_BO")
    private String itemBo;

    @TableField("WORK_STEP_CODE_BO")
    private String workStepCodeBo;

    @Excel( name="工步编码", orderNum="3" )
    @TableField(exist = false)
    private String workStepCode;

    @TableField("OPERATION_BO")
    private String operationBo;

    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @TableField("OPERATION_ORDER")
    @Excel( name="工序工单", orderNum="9" )
    private String operationOrder;

    @TableField("USER_BO")
    private String userBo;

    @TableField("QTY")
    @Excel( name="数量", orderNum="6" )
    private BigDecimal qty;

    @TableField("DIF_QTY")
    @Excel( name="差异数量", orderNum="13" )
    private BigDecimal difQty;

    @TableField("ME_SFC_WIP_LOG_BO")
    private String meSfcWipLogBo;

    @TableField("STATE")
    private String state;

    @TableField("TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel( name="时间", orderNum="7",exportFormat = "yyyy-MM-dd HH:mm:ss" ,importFormat = "yyyy-MM-dd HH:mm:ss" )
    private Date time;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    @Excel( name="报工人员", orderNum="5" )
    private String userName;

    @TableField(exist = false)
    @ApiModelProperty("工单")
    @Excel( name="工单", orderNum="8" )
    private String shopOrder;

    @TableField(exist = false)
    @ApiModelProperty("物料编码")
    @Excel( name="物料编码", orderNum="10" )
    private String item;

    @TableField(exist = false)
    @ApiModelProperty("物料名称")
    @Excel( name="物料名称", orderNum="11" )
    private String itemName;

    @TableField(exist = false)
    @ApiModelProperty("物料描述")
    @Excel( name="物料描述", orderNum="12" )
    private String itemDesc;

    @TableField(exist = false)
    @ApiModelProperty("工序名称")
    @Excel( name="工序", orderNum="4" )
    private String operationName;

    @TableField(exist = false)
    @ApiModelProperty("sfc数量")
    @Excel( name="sfc数量", orderNum="2" )
    private String sfcQty;

    @TableField("STEP_DISPATCH_CODE")
    @ApiModelProperty("派工单编码（线圈报工使用）")
    private String stepDispatchCode;

    @TableField("CREATE_DATE")
    @ApiModelProperty("创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createTime;

    @TableField("UPDATE_DATE")
    @ApiModelProperty("修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date updateDate;

    @TableField("UPDATE_BY")
    @ApiModelProperty("修改人")
    private String updateBy;

    @TableField(exist = false)
    private String workStepName;

    @TableField("OTHER_USER")
    @ApiModelProperty("其他报工人")
    private String otherUser;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
