package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OpThroughRateDTO",description = "工序直通率DTO")
public class OpThroughRateDTO {

    @ApiModelProperty("分页对象")
    private Page page;

    @ApiModelProperty("工单bo")
    private String shopOrderBo;

    @ApiModelProperty("物料bo")
    private String itemBo;

    @ApiModelProperty("车间bo")
    private String workShopBo;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    private String operationBo;

    private String state;

}
