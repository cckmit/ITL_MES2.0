package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value="SfcDevice",description="任务设备对应表")
public class SfcDeviceVO{

    @ApiModelProperty(value="设备BO")
    private String deviceBo;

    @ApiModelProperty(value="设备名称")
    private String deviceName;

    @ApiModelProperty(value="设备描述")
    private String deviceDesc;

    @ApiModelProperty(value="设备编码")
    private String device;

    @ApiModelProperty(value="批次条码")
    private String sfc;

    @ApiModelProperty(value="工单BO")
    private String shopOrderBo;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inTime;

    private String selectInTime;

    @ApiModelProperty(value = "工序工单")
    private String operationOrder;

    @ApiModelProperty(value = "进站人")
    private String userBo;

    @ApiModelProperty(value = "进站人名称")
    private String realName;

    @ApiModelProperty(value = "派工单编码")
    private String dispatchCode;

    @ApiModelProperty(value = "当前生产工序")
    private String operationBo;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value="工序名称")
    @TableField(exist = false)
    private String operationName;

    @ApiModelProperty(value="工单")
    @TableField(exist = false)
    private String shopOrder;
    Page page;
}
