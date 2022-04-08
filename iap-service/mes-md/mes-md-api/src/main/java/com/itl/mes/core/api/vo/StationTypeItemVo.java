package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "StationTypeItemVo",description = "工位类型中保存工位用")
public class StationTypeItemVo implements Serializable {

    @ApiModelProperty(value="工位【UK】")
    private String station;

    @ApiModelProperty(value="工位名称")
    private String stationName;
}
