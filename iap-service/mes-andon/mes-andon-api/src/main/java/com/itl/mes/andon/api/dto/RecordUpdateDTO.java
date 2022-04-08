package com.itl.mes.andon.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2020/12/27
 */
@Data
@ApiModel(value = "RecordUpdateDTO",description = "异常日志修改实体")
public class RecordUpdateDTO {

    @ApiModelProperty(value = "异常表ID")
    private String pid;

    @ApiModelProperty(value = "修复照片")
    private String repairImg;

    @ApiModelProperty(value = "修复备注")
    private String repairRemark;

}
