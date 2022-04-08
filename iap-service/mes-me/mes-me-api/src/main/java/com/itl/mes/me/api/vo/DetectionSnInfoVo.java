package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "DetectionInfoVo", description = "SN生命周期 检测记录")
public class DetectionSnInfoVo implements Serializable {

    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "检测状态")
    private String state;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码描述")
    private String ncDesc;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "不良位置")
    private String componentPosition;

    @ApiModelProperty(value = "检测时间")
    private Date createDate;

    @ApiModelProperty(value = "操作人")
    private String userBo;

    @ApiModelProperty(value = "备注")
    private String remark;

}
