package com.itl.mes.me.api.dto.snLifeCycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yaoxiang
 * @date 2021/1/27
 * @since JDK1.8
 */
@Data
@ApiModel("SN-Bom")
public class BomInfo {
    @ApiModelProperty("物料编码")
    private String item;
    @ApiModelProperty("用量")
    private BigDecimal qty;
    @ApiModelProperty("物料版本")
    private String version;
    @ApiModelProperty("装配工序")
    private String operation;
    @ApiModelProperty("位置")
    private String position;
    @ApiModelProperty("追溯方式")
    private String zsType;
    @ApiModelProperty("编码规则")
    private String codeRuleType;
}
