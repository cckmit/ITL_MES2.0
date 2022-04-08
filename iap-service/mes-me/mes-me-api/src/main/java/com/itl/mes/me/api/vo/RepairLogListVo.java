package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "RepairLogListVo", description = "维修工位 维修明细")
public class RepairLogListVo implements Serializable {

    @ApiModelProperty(value = "不良位置")
    private String componentPosition;

    @ApiModelProperty(value = "不良物料编码:不良组件的item")
    private String item;

    @ApiModelProperty(value = "维修原因(字典维护)")
    private String repairReason;

    @ApiModelProperty(value = "维修方式")
    private String repairMethod;

    @ApiModelProperty(value = "不良组件SN")
    private String badItemSn;

    @ApiModelProperty(value = "替换组件SN")
    private String replaceItemSn;

    @ApiModelProperty(value = "责任单位")
    private String dutyUnit;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "维修时间")
    private Date repairTime;
}
