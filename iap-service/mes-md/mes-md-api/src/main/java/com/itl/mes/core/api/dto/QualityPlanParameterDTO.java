package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "检验计划明细查询对象")
public class QualityPlanParameterDTO {

    //分页对象
    private Page page;

    @ApiModelProperty(value="质量控制计划")
    private String qualityPlan;

    @ApiModelProperty(value="版本")
    private String version;

    @ApiModelProperty(value = "所属工序")
    private String operationName;

    @ApiModelProperty(value = "产品类别")
    private String itemName;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "检验项名称")
    private String parameterDesc;
}
