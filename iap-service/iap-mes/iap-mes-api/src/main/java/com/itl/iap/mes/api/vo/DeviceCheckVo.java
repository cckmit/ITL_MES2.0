package com.itl.iap.mes.api.vo;

import com.itl.iap.mes.api.entity.DeviceCheck;
import com.itl.iap.mes.api.entity.DeviceCheckItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "DeviceCheckVo",description = "设备点检明细返回实体")
public class DeviceCheckVo {

    @ApiModelProperty(value = "设备点检信息")
    private DeviceCheck deviceCheck;

    @ApiModelProperty(value = "设备点检明细信息")
    private List<DeviceCheckItem> deviceCheckItems;
}
