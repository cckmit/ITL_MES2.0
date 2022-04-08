package com.itl.mes.pp.api.dto.schedule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "ScheduleReceiveRespDTO",description = "派工接收返回实体，用于查询派工接收数据")
public class ScheduleReceiveRespDTO {

    @ApiModelProperty(value = "排程接收BO")
    private String bo;

    @ApiModelProperty(value = "排程号")
    private String scheduleNo;

    @ApiModelProperty(value = "工单号")
    private String shopOrder;

    @ApiModelProperty(value = "优先级")
    private String priority;

    @ApiModelProperty(value = "排程数量")
    private BigDecimal scheduleQty;

    @ApiModelProperty(value = "产线名称")
    private String productionLineName;

    @ApiModelProperty(value = "工位名称")
    private String stationName;


    @ApiModelProperty(value = "已接收数量")
    private BigDecimal receiveQty;

    @ApiModelProperty(value = "物料清单")
    private String bom;

    @ApiModelProperty(value = "物料清单版本")
    private String bomVersion;

    @ApiModelProperty(value = "物料")
    private String  item;

    @ApiModelProperty(value = "物料版本")
    private String itemVersion;

    @ApiModelProperty(value = "工艺路线")
    private String router;

    @ApiModelProperty(value = "工艺路线版本")
    private String routerVersion;

}
