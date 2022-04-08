package com.itl.mes.pp.api.dto.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/16 11:24
 */
@Data
@ApiModel(value = "排程接收查询实体")
public class ScheduleReceiveQueryDTO {



    @ApiModelProperty(value = "分页对象")
    private Page page;


    @ApiModelProperty(value ="车间")
    private String workShopName;

    @ApiModelProperty(value ="产线")
    private String productionLineName;

    @ApiModelProperty(value ="工单号")
    private String shopOrderName;

    @ApiModelProperty(value ="排程号")
    private String scheduleNo;

    @ApiModelProperty(value = "物料")
    private String item;


    /**
     * 以下几个参数是为查询排程接收表的数据，即派工接收的完成信息和历史信息
     */
    @ApiModelProperty(value ="接收开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date receiveStartDate;

    @ApiModelProperty(value ="接收结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date receiveEndDate;


    @ApiModelProperty(value = "是否完成，是则查询完成信息，否则查询全部信息")
    private Boolean isComplete;

}
