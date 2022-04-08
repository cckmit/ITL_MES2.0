package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "BomVo",description = "保存物料清单用")
public class BomVo implements Serializable {

    @ApiModelProperty(value="BOM:SITE,BOM,VERSION【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="物料清单编号【UK】")
    private String bom;

    @ApiModelProperty(value="版本【UK】")
    private String version;

    @ApiModelProperty(value="BOM类型 ")
    private String bomType;
    @ApiModelProperty(value="工单编号")
    private String shopOrder;
    @ApiModelProperty(value="bom基准用量")
    private String bomStandard;
    @ApiModelProperty(value="erp bom")
    private String erpBom;

    @ApiModelProperty(value="是否当前版本")
    private String isCurrentVersion;

    @ApiModelProperty(value="物料清单描述")
    private String bomDesc;

    @ApiModelProperty(value="状态【M_STATUS的】")
    private String state;

    @ApiModelProperty(value="追溯方式")
    private String zsType;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value="bom组件对象集合")
    private List<BomComponnetVo> bomComponnetVoList;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;

    private String createUser;
}
