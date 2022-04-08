package com.itl.mes.pp.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @auth liuchenghao
 * @date 2021/1/20
 */
@Data
@ApiModel(value = "CapacityLoadReportDTO",description = "产能负荷报表")
public class CapacityLoadReportDTO {

    @ApiModelProperty(value = "产线，即机台")
    Set<String> productLines;

    @ApiModelProperty(value = "时间集合")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    List<Date> dateList;

    @ApiModelProperty(value = "产能负荷数据集合")
    List<CapacityLoadRespDTO> capacityLoadRespDTOList;
}
