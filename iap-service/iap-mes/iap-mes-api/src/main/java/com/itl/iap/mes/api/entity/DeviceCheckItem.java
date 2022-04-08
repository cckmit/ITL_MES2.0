package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("m_device_check_item")
@ApiModel(value="DeviceCheckItem",description="设备点检明细表")
public class DeviceCheckItem extends Model<DeviceCheckItem>{


    private static final long serialVersionUID = 2697481270786011918L;

    @ApiModelProperty(value="id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value="项目名称")
    @TableField("item_name")
    private String itemName;

    @ApiModelProperty(value="项目描述")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value="操作")
    @TableField("operation")
    private String operation;

    @ApiModelProperty(value="备注")
    @TableField("comments")
    private String comments;

    @ApiModelProperty(value="点检单编号")
    @TableField("device_check_code")
    private String devicedCheckCode;

    @ApiModelProperty(value="巡检结果")
    @TableField("inspection_result")
    private String inspectionResult;

    @ApiModelProperty(value="巡检评价")
    @TableField("inspection_evaluate")
    private String inspectionEvaluate;




    @Override
    protected Serializable pkVal() {
        return null;
    }
}
