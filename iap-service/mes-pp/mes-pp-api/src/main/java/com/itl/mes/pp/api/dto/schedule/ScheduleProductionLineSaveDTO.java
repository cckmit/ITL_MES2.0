package com.itl.mes.pp.api.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/12 14:47
 */
@Data
@ApiModel(value = "排程产线保存实体")
public class ScheduleProductionLineSaveDTO {

    @ApiModelProperty(value ="排程产线BO")
    private String bo;

    @ApiModelProperty(value ="产线BO")
    private String productionLineBo;

    @ApiModelProperty(value ="产线名称")
    private String productionLineName;

    @ApiModelProperty(value ="工位BO")
    private String stationBo;

    @ApiModelProperty(value ="工位名称")
    private String stationName;

    @ApiModelProperty(value ="数量")
    private Integer quantity;

    @ApiModelProperty(value ="开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value ="结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value ="排程BO")
    private String scheduleBo;


}
