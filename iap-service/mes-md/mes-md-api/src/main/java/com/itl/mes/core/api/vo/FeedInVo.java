package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value="FeedInVo",description="上料维护")
public class FeedInVo {

    @ApiModelProperty(value = "设备Bo")
    private String deviceBo;

    @ApiModelProperty(value = "设备编码")
    private String device;

    @ApiModelProperty(value = "工单Bo")
    private String shopOrderBo;
    @ApiModelProperty(value = "工单编码")
    private String shopOrder;
    @ApiModelProperty(value = "工单物料Bo")
    private String itemBo;
    @ApiModelProperty(value = "工单物料")
    private String item;

    @ApiModelProperty(value = "sfc")
    private String sfc;
    @ApiModelProperty(value = "sfc上料数量")
    private int sfcQty;
    @ApiModelProperty(value = "sfc出站数量")
    private int sfcOutQty;
    @ApiModelProperty(value = "sfc剩余数量")
    private int surplusQty;
    @ApiModelProperty(value = "sfc物料Bo")
    private String sfcItemBo;
    @ApiModelProperty(value = "sfc物料编码")
    private String sfcItem;
    @ApiModelProperty(value = "sfc物料名称")
    private String sfcItemName;
    @ApiModelProperty(value = "sfc物料描述")
    private String sfcItemDesc;

    @ApiModelProperty(value = "bom")
    private String bomBo;
    @ApiModelProperty(value = "bom用量")
    private int bomQty;
}
