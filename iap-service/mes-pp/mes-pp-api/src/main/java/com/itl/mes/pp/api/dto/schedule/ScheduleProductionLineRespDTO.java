package com.itl.mes.pp.api.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/13 14:02
 */
@Data
@ApiModel(value = "排程产线查询返回实体")
public class ScheduleProductionLineRespDTO {

    @ApiModelProperty(value ="排程产线关联表BO")
    private String bo;

   @ApiModelProperty(value ="产线编号")
    private String productionLineBo;

   @ApiModelProperty(value ="产线名称")
    private String productionLineName;

   @ApiModelProperty(value ="工位编号")
    private String stationBo;

   @ApiModelProperty(value ="工位名称")
    private String stationName;


    @ApiModelProperty(value ="开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value ="结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value ="数量")
    private BigDecimal quantity;


}
