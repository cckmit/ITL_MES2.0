package com.itl.mes.pp.api.dto.schedule;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "ScheduleReceiveDTO",description = "派工接收实体")
public class ScheduleReceiveDTO {

    @ApiModelProperty(value = "派工接收Bo")
    private String bo;


    @ApiModelProperty(value = "接收数量")
    private BigDecimal receiveQty;


}
