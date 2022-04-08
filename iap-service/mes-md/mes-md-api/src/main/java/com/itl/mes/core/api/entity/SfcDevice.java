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
import java.util.Date;

@Data
@TableName("me_sfc_device")
@ApiModel(value="SfcDevice",description="任务设备对应表")
public class SfcDevice extends Model<SfcDevice> {

    @ApiModelProperty(value="设备BO")
    @TableId(value = "device_bo", type = IdType.INPUT)
    private String deviceBo;

    @ApiModelProperty(value="批次条码")
    @TableField("sfc")
    private String sfc;

    @ApiModelProperty(value="工单BO")
    @TableField("shop_order_bo")
    private String shopOrderBo;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_date")
    private Date createDate;

    @ApiModelProperty(value = "工序工单")
    @TableField("operation_order")
    private String operationOrder;

    @ApiModelProperty(value = "进站人")
    @TableField("user_bo")
    private String userBo;

    @ApiModelProperty(value = "派工单编码")
    @TableField("dispatch_code")
    private String dispatchCode;

    @ApiModelProperty(value = "当前生产工序")
    @TableField("operation_bo")
    private String operationBo;
    
    @Override
    protected Serializable pkVal() {
        return this.deviceBo;
    }
}
