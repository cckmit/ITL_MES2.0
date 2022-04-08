package com.itl.mes.pp.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 崔翀赫
 * @date 2020/12/21$
 * @since JDK1.8
 */
@Data
@ApiModel("机台产能基础信息查询条件")
public class ProductLineDto {

    @ApiModelProperty(value = "车间")
    String workShop;
    @ApiModelProperty(value = "产线")
    String productLineBo;
    @ApiModelProperty(value = "每页显示条数")
    int limit;
    @ApiModelProperty(value = "页数")
    int page;
}
