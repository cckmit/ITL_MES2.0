package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "StationTypeVo",description = "保存工位类型用")
public class StationTypeVo implements Serializable {


    @ApiModelProperty(value="ST:SITE,STATION_TYPE【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="编号【UK】")
    private String stationType;

    @ApiModelProperty(value="名称")
    private String stationTypeName;

    @ApiModelProperty(value="描述")
    private String stationTypeDesc;


    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "可分配工位")
    private List<StationTypeItemVo> availableStationTypeItemVos;

    @ApiModelProperty(value = "已分配工位")
    private List<StationTypeItemVo> assignedStationTypeItemVos;

}
