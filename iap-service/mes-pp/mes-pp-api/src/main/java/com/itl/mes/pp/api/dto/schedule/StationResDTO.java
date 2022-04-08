package com.itl.mes.pp.api.dto.schedule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "StationResDTO",description = "工位结果返回实体")
public class StationResDTO {


    @ApiModelProperty(value = "工位bo")
    private String stationBo;

    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "工位描述")
    private String stationName;

}
