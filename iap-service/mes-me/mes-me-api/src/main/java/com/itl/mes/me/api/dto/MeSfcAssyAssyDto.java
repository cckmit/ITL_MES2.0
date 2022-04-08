package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yaoxiang
 * @date 2021/1/28
 * @since JDK1.8
 */
@Data
@ApiModel("扫描成品SN参数列表")
public class MeSfcAssyAssyDto {
    @ApiModelProperty("成品SN")
    private String sn;
    @ApiModelProperty("排程号")
    private String scheduleNo;
    @ApiModelProperty("物料编码")
    private String item;
    @ApiModelProperty("物料版本")
    private String itemVersion;
    @ApiModelProperty("sfc工序步骤")
    private String sfcStep;
    @ApiModelProperty("排程计划量")
    private BigDecimal sfcQty;
    @ApiModelProperty("sfc投入数量")
    private BigDecimal receiveQty;
}
