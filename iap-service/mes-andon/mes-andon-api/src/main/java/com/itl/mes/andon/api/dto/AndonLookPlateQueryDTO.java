package com.itl.mes.andon.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2020/12/25
 */
@Data
@ApiModel(value = "AndonLookPlateQueryDTO",description = "安灯看板查询实体")
public class AndonLookPlateQueryDTO {

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "车间")
    private String workShopBo;

}
