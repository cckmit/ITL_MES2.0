package com.itl.mes.pp.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/1/7
 */
@Data
@ApiModel(value = "CapacityLoadRespDTO",description = "产能负荷数据")
public class CapacityLoadRespDTO {


    @ApiModelProperty(value = "机台名称")
    private String productLine;


    @ApiModelProperty(value = "额定时间")
    private Double ratedTime;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    @ApiModelProperty(value = "可用工时")
    private Double availableWorkHour;

    @ApiModelProperty(value = "已排工时")
    private Double arrangedWorkHour;

    @ApiModelProperty(value = "剩余工时")
    private Double surplusWorkHour;


}
