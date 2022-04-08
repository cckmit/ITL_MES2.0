package com.itl.mes.pp.api.dto.schedule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/12 17:43
 */
@Data
@ApiModel(value = "排程查询返回实体")
public class ScheduleRespDTO {


    @ApiModelProperty(value = "排程BO，主键")
    private String bo;

    @ApiModelProperty(value = "排程号")
    private String scheduleNo;


    @ApiModelProperty(value ="工单号")
    private String shopOrder;

    @ApiModelProperty(value ="工单号集合，用于查询单条排程信息回显工单号")
    private List<String> shopOrders;


    @ApiModelProperty(value ="物料号")
    private String item;

    @ApiModelProperty(value ="物料版本")
    private String itemVersion;

    @ApiModelProperty(value ="物料清单号")
    private String bom;

    @ApiModelProperty(value ="物料清单版本")
    private String bomVersion;


    @ApiModelProperty(value ="工艺路线")
    private String router;

    @ApiModelProperty(value ="物料清单版本")
    private String routerVersion;


    @ApiModelProperty(value ="物料清单号")
    private String quantity;


    @ApiModelProperty(value ="排程类型")
    private String scheduleType;

    @ApiModelProperty(value = "排程状态，1为创建，2为下达，3为接收")
    private Integer state;

    @ApiModelProperty(value ="控制状态")
    private Integer controlState;


}
