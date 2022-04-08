package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "StationVo",description = "保存工位数据")
public class StationVo implements Serializable {
    @ApiModelProperty(value="WC:SITE,WORK_SHOP,PRODUCT_LINE,OPERATION,STATION【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="工位【UK】")
    private String station;

    @ApiModelProperty(value="工位名称")
    private String stationName;

    @ApiModelProperty(value="工位描述")
    private String stationDesc;

    @ApiModelProperty(value="工位类型")
    private String stationType;

    @ApiModelProperty(value="所属产线")
    private String productLine;

    @ApiModelProperty(value="所属工序编号【UK】")
    private String operation;

    @ApiModelProperty(value="所属工序版本")
    private String operationVersion;

    @ApiModelProperty(value="状态（已启用1，已禁用0）")
    private String state;


    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;

    @ApiModelProperty( value = "工步bo" )
    private String workstationBo;
}
