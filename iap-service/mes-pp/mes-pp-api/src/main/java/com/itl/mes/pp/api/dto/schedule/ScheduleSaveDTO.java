package com.itl.mes.pp.api.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/12 14:37
 */
@Data
@ApiModel(value = "排程保存请求实体")
public class ScheduleSaveDTO {


    @ApiModelProperty(value = "排程BO，主键")
    private String bo;

    @ApiModelProperty(value = "排程号")
    private String scheduleNo;


    @ApiModelProperty(value ="工单号集合")
    private List<String> shopOrders;

    @ApiModelProperty(value ="物料号")
    private String itemBo;


    @ApiModelProperty(value ="物料清单号")
    private String bomBo;



    @ApiModelProperty(value ="车间")
    private String workshopBo;

    @ApiModelProperty(value ="排程类型")
    private String scheduleType;

    @ApiModelProperty(value = "排程状态，1为创建，2为下达，3为接收")
    private Integer state;

    @ApiModelProperty(value ="工艺路线")
    private String routerBo;


    @ApiModelProperty(value ="控制状态")
    private Integer controlState;

    @ApiModelProperty(value ="优先级")
    private String priority;

    @ApiModelProperty(value ="开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value ="结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;


    @ApiModelProperty(value ="排程数量")
    private BigDecimal quantity;

    @ApiModelProperty(value ="工厂")
    private String site;

    @ApiModelProperty(value ="产能分配")
    private List<ScheduleProductionLineSaveDTO> scheduleProductionLineSaveDTOList;
    
}
