package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StepNcRecordDto {
    @ApiModelProperty(value = "分页信息")
    private Page page;
}
