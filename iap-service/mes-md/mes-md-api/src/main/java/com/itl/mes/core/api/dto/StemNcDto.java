package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "StemNcDto",description = "线圈不良登记dto")
public class StemNcDto {
    @ApiModelProperty("工单bo")
    private String shopOrderBo;
    @ApiModelProperty("物料bo")
    private String itemBo;
    @ApiModelProperty("不良详情集合")
    private List<StemNcDetailsDto> stemNcDetailsDtos;

    @Data
    @ApiModel(value = "StemNcDetailsDto",description = "不良详情Dto")
    public static class StemNcDetailsDto{
        @ApiModelProperty("不良代码bo")
        private String ncCodeBo;
        @ApiModelProperty("不良数量")
        private BigDecimal ncQty;
        @ApiModelProperty("不良类型（0：不良 1：报废）")
        private String ncType;
        @ApiModelProperty("不良工步bo")
        private String ncWorkStepBo;
        @ApiModelProperty("责任人")
        private String personLiable;
        @ApiModelProperty("不良项目")
        private String ncProject;
        @ApiModelProperty("内部工号")
        private String insideNo;
    }
}
