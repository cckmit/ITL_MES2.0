package com.itl.mes.andon.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @auth liuchenghao
 * @date 2020/12/23
 */
@Data
@ApiModel(value = "RecordSaveDTO",description = "")
public class RecordSaveDTO {


    @ApiModelProperty(value = "安灯的资源类型")
    private String resourceType;

    @ApiModelProperty(value = "物料BO，可不传")
    private String itemBo;

    @ApiModelProperty(value = "安灯BO")
    private String andonBo;

    @ApiModelProperty(value = "设备BO，可不传")
    private String deviceBo;

    @ApiModelProperty(value = "故障代码BO")
    private String faultCodeBo;

    @ApiModelProperty(value = "叫料数量")
    private BigDecimal callQuantity;

    @ApiModelProperty(value = "异常备注")
    private String abnormalRemark;

    @ApiModelProperty(value = "异常图片")
    private String abnormalImg;
}
