package com.itl.mes.pp.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2021/1/7
 */
@Data
@ApiModel(value = "CapacityLoadQueryDTO",description = "产能负荷报表查询请求实体")
public class CapacityLoadQueryDTO {

    @ApiModelProperty(value = "车间BO")
    private String  workShopBo;


    @ApiModelProperty(value = "时间范围")
    private Integer DateRange;


}
