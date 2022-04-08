package com.itl.mes.core.api.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "CarrierTypeVo",description = "保存载具类型VO")
public class CarrierTypeVo implements Serializable {

    @ApiModelProperty(value="CT:SITE,CARRIER_TYPE[PK-]")
    private String bo;

    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="载具类型")
    private String carrierType;

    @ApiModelProperty(value="载具描述")
    private String description;

    @ApiModelProperty(value="容量")
    private Integer capacity;

    @ApiModelProperty(value="行数")
    private Integer rowSize;

    @ApiModelProperty(value="列数")
    private Integer columnSize;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "载具物料明细")
    private List<CarrierItemVo> carrierItemVos;

    @ApiModelProperty(value = "是否允许物料混装 (Y/N)")
    private String isBlend;
}
