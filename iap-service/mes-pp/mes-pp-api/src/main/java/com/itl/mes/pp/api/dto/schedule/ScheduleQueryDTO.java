package com.itl.mes.pp.api.dto.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/11/11 19:08
 */
@Data
@ApiModel(value = "排程查询请求实体")
public class ScheduleQueryDTO {


    @ApiModelProperty(value = "分页对象")
    private Page page;


    @ApiModelProperty(value = "排程号")
    private String scheduleNo;


    @ApiModelProperty(value = "工单号")
    private String shopOrder;


    @ApiModelProperty(value = "物料号")
    private String item;

    @ApiModelProperty(value = "物料版本")
    private String itemVersion;

    @ApiModelProperty(value = "工厂")
    private String site;
}
