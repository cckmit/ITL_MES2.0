package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SfcDataStatisticsDto", description = "sfc完成数据统计")
public class SfcDataStatisticsDto {
    @ApiModelProperty(value = "分页信息")
    private Page page;
}
