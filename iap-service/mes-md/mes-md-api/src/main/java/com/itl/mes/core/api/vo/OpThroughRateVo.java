package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "OpThroughRateVo",description = "工序直通率VO")
public class OpThroughRateVo {

    @ApiModelProperty("工序bo")
    private String operationBo;

    @ApiModelProperty("工序")
    private String operation;

    @ApiModelProperty("工序名称")
    private String operationName;

    @ApiModelProperty("进站数量")
    private BigDecimal inputQty;

    @ApiModelProperty("出站数量")
    private BigDecimal doneQty;

    @ApiModelProperty("不良总数量")
    private BigDecimal ncTotalQty;

    @ApiModelProperty("报废数量")
    private BigDecimal scrapQty;

    @ApiModelProperty("直通率")
    private String throughRate;

    @ApiModelProperty("总直通率")
    private BigDecimal throughRateTotal;

    @ApiModelProperty("不良详情")
    private List<NcDetails> ncDetailsList;

    @ApiModelProperty("报废明细")
    private List<ScrapDetails> scrapDetailsList;

    @Data
    @ApiModel(value = "不良明细")
    public static class NcDetails{
        @ApiModelProperty("批次条码")
        private String sfc;
        @ApiModelProperty("不合格代码")
        private String ncCode;
        @ApiModelProperty("不合格项")
        private String ncName;
        @ApiModelProperty("数量")
        private BigDecimal ncQty;
        @ApiModelProperty("设备编码")
        private String device;
        @ApiModelProperty("设备名称")
        private String deviceName;
        @ApiModelProperty("用户id")
        private String userId;
        @ApiModelProperty("用户名称")
        private String userName;
    }

    @Data
    @ApiModel(value = "报废明细")
    public static class ScrapDetails{

        @ApiModelProperty("批次条码")
        private String sfc;
        @ApiModelProperty("不合格代码")
        private String ncCode;
        @ApiModelProperty("不合格项")
        private String ncName;
        @ApiModelProperty("数量")
        private BigDecimal ncQty;
        @ApiModelProperty("设备编码")
        private String device;
        @ApiModelProperty("设备名称")
        private String deviceName;
        @ApiModelProperty("用户id")
        private String userId;
        @ApiModelProperty("用户名称")
        private String userName;

    }
}
