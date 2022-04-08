package com.itl.mes.pp.api.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "ScheduleDetailRespDTO",description = "查询单个排程信息的返回实体")
public class ScheduleDetailRespDTO {


    @ApiModelProperty(value = "排程BO，主键")
    private String bo;

    @ApiModelProperty(value = "排程号")
    private String scheduleNo;



    @ApiModelProperty(value ="工单号集合，用于查询单条排程信息回显工单号")
    private List<String> shopOrderBos;


    @ApiModelProperty(value ="数量")
    private String quantity;


    @ApiModelProperty(value ="排程类型")
    private String scheduleType;

    @ApiModelProperty(value = "排程状态，1为创建，2为下达，3为接收")
    private Integer state;

    @ApiModelProperty(value ="物料BO")
    private String itemBo;

    @ApiModelProperty(value ="物料清单BO")
    private String bomBo;

    @ApiModelProperty(value ="车间BO")
    private String workshopBo;

    @ApiModelProperty(value ="工艺路线BO")
    private String routerBo;

    @ApiModelProperty(value ="工单BO")
    private String shopOrderBo;

    @ApiModelProperty(value ="控制状态")
    private String controlState;

    @ApiModelProperty(value ="开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value ="结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty("接收数量")
    private BigDecimal receiveQty;
}
