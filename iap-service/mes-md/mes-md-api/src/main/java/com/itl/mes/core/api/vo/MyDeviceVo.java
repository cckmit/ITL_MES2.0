package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.itl.mes.core.api.entity.DeviceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value="MyDeviceVo",description="人员设备")
public class MyDeviceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "设备bo")
    private String deviceBo;

    @ApiModelProperty(value = "设备编码")
    private String device;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "0：未点检 1：已点检")
    private String spotState;

    @ApiModelProperty(value = "0：未首检 1：已首检")
    private String firstInsState;

    @ApiModelProperty("设备类型")
    List<DeviceType> deviceTypeList;

    @ApiModelProperty("工单")
    private String shopOrder;

    @ApiModelProperty("物料编码")
    private String item;

    @ApiModelProperty("物料描述")
    private String itemDesc;

    @ApiModelProperty("当前工单在当前工序完成的数量")
    private BigDecimal doneQty;

    @ApiModelProperty("工单数量")
    private BigDecimal shopOrderQty;

    @ApiModelProperty("工序工单")
    private String operationOrder;

    @ApiModelProperty("派工单编码")
    private String dispatchCode;

    @ApiModelProperty("派工数量")
    private int dispatchQty;

    @ApiModelProperty("派工单数量")
    private int dispatchCodeQty;

    @ApiModelProperty(value = "当前生产sfc")
    private String sfc;
}
