package com.itl.mes.core.api.dto;

import com.itl.mes.core.api.entity.AutomaticReportWork;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.DeviceNc;
import com.itl.mes.core.api.entity.OutStationDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "OutStationDto",description = "出站传入参数")
public class OutStationDto {

    @ApiModelProperty(value = "sfc条码")
    private String sfc;

    @ApiModelProperty(value = "工单")
    private String shopOrder;

    @ApiModelProperty(value = "选中设备集合(扫描sfc带出相关信息传入)")
    private List<Device> devices;

    @ApiModelProperty(value = "不良或报废记录集合")
    private List<DeviceNc> deviceNcs;

    @ApiModelProperty(value = "出站设备记录集合")
    private List<OutStationDevice> outStationDevices;

    private BigDecimal inputQty;

    @ApiModelProperty(value = "检验工序出站良品")
    private BigDecimal testOpDoneQty;

    List<AutomaticReportWork> automaticReportWorkList;
}
