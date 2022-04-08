package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "BomComponnetVo",description = "保存物料清单组件用")
public class BomComponnetVo implements Serializable {

    @ApiModelProperty(value="BC:SITE,BOM,COMPONENT【PK】")
    private String bo;
    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="BOM【M_BOM的】【UK】【FK-】")
    private String bom;

    @ApiModelProperty(value="编号【UK】")
    private Integer sequence;

    @ApiModelProperty(value="组件【M_ITEM的item】")
    private String component;

    @ApiModelProperty("组件的描述")
    private String itemDesc;

    @ApiModelProperty(value="组件版本【M_ITEM的VERSION】")
    private String itemVersion;

    @ApiModelProperty(value="物料单位【M_ITEM_UNIT】")
    private String itemUnit;

    @ApiModelProperty(value="装配工序【M_OPERATION的operation】")
    private String operation;

    @ApiModelProperty(value="工序版本【M_OPERATION的Version】")
    private String operationVersion;

    @ApiModelProperty(value="是否使用替代组件")
    private String isUseAlternateComponent;

    @ApiModelProperty(value="组件类型")
    private String itemType;
    @ApiModelProperty(value="组件装配顺序")
    private String itemOrder;
    @ApiModelProperty(value="虚拟件支持")
    private String virtualItem;


    @ApiModelProperty(value="追溯方式")
    private String zsType;

    @ApiModelProperty(value="装配类型")
    private String assType;

    @ApiModelProperty(value="组件位置")
    private String componentPosition;

    @ApiModelProperty(value="参考指示符")
    private String reference;

    @ApiModelProperty(value="是否有子BOM[备用字段无需上传]")
    private String hasSubBom;

    @ApiModelProperty(value="是否有子BOM[备用字段无需上传]")
    private String subBom;

    @ApiModelProperty(value="装配数量")
    private BigDecimal qty;

    @ApiModelProperty("物料条码")
    private String itemSn;

    @ApiModelProperty("是否装配完成")
    private boolean assyFinish;
}
