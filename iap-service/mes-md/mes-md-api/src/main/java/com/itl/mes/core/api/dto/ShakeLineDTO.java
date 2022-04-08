package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShakeLineDTO {
    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "物料")
    private String item;

    @ApiModelProperty(value = "物料版本")
    private String version;
}
