package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "WarehouseVo",description = "保存线边仓用")
public class WarehouseVo implements Serializable {

    @ApiModelProperty(value="WAREHOUSE:SITE,VARHOUSE【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK-】")
    private String site;

    @ApiModelProperty(value="线边仓编号【UK-】")
    private String warehouse;

    @ApiModelProperty(value="线边仓名称")
    private String warehouseName;

    @ApiModelProperty(value="线边仓描述")
    private String warehouseDesc;

    @ApiModelProperty(value="线边仓类别")
    private String warehouseType;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;
}
