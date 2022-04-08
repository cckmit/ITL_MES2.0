package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 维修工位 顶部所需数据Vo
 */
@Data
@ApiModel(value = "RepairStationVo", description = "维修工位产品上游数据展示")
public class RepairStationVo implements Serializable {

    @ApiModelProperty(value = "sfc表的sfc字段")
    private String sfc;

    @ApiModelProperty(value = "工单")
    private String shopOrder;

    @ApiModelProperty(value = "产线")
    private String productLine;

    @ApiModelProperty(value = "排程号")
    private String scheduleNo;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "送修工序-(问题工序)")
    private String operation;

    @ApiModelProperty(value = "送修工位-(问题工位)")
    private String station;

    @ApiModelProperty(value = "送修人")
    private String userBo;

    @ApiModelProperty(value = "送修时间")
    private Date recordTime;

    @ApiModelProperty(value = "送修详情")
    List<SendRepairDetailsVo> sendRepairDetailsVo;

    @ApiModelProperty(value = "维修明细")
    List<RepairLogListVo> repairLogListVo;

    @ApiModelProperty(value = "累计维修数量")
    private Integer repairCount;
}
