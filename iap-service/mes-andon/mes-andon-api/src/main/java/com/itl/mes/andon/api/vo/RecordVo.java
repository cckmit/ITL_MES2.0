package com.itl.mes.andon.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/24
 */
@Data
@ApiModel(value = "RecordVo",description = "异常日志信息返回实体")
public class RecordVo {

    @ApiModelProperty(value = "触发人")
    private String triggerMan;


    @ApiModelProperty(value = "触发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String triggerTime;


    @ApiModelProperty(value = "设备故障代码")
    private String faultCodeBo;


    @ApiModelProperty(value = "叫料数量")
    private BigDecimal callQuantity;

    @ApiModelProperty(value = "异常图片")
    private String abnormalImg;


    @ApiModelProperty(value = "异常备注")
    private String abnormalRemark;

    @ApiModelProperty(value = "设备编号")
    private String device;

    @ApiModelProperty(value = "物料编号")
    private String item;

    @ApiModelProperty(value = "异常图片,base64文件")
    private List<String> imgs;
}
