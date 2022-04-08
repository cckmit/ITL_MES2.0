package com.itl.mes.pp.api.dto.schedule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuchenghao
 * @date 2020/11/16 19:18
 */
@Data
@ApiModel(value = "排程待接收查询返回实体")
public class ReceiveRespDTO {

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


}
