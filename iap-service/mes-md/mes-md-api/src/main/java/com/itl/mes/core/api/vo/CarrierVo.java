package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "CarrierVo",description = "保存载具用")
public class CarrierVo implements Serializable {

    @ApiModelProperty(value="CARRIER:SITE,CARRIER[PK-]")
    private String bo;

    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="载具")
    private String carrier;

    @ApiModelProperty(value="描述")
    private String description;

    @ApiModelProperty(value="载具类型")
    private String carrierType;

    @ApiModelProperty(value="使用次数")
    private Integer useCount;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

}
