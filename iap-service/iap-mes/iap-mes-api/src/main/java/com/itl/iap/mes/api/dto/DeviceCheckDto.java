package com.itl.iap.mes.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.entity.CheckPlan;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import com.itl.iap.mes.api.entity.DeviceCheck;
import com.itl.iap.mes.api.entity.DeviceCheckItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "设备点检单信息")
public class DeviceCheckDto {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "设备点检计划")
    CheckPlan checkPlan;

    @ApiModelProperty(value = "设备点检单编号")
    private String deviceCheckCode;

    @ApiModelProperty(value = "设备点检明细信息")
    private List<DeviceCheckItem> deviceCheckItems;
    @ApiModelProperty(value = "收集组项目信息")
    private List<DataCollectionItem> dataCollectionItems;

    @ApiModelProperty("点检时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date checkTime;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    @ApiModelProperty(value = "设备点检人名字")
    private String checkRealName;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date endDate;


}
