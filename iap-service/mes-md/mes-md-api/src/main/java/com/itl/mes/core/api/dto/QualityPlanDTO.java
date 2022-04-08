package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "检验计划查询")
public class QualityPlanDTO {

    //分页对象
    private Page page;
    private String qualityPlan;
    private String qualityPlanDesc;
}
