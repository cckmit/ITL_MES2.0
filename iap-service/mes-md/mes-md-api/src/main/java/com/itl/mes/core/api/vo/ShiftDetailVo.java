package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "ShiftDetailVo",description = "保存班次明细用")
public class ShiftDetailVo implements Serializable {

    @ApiModelProperty(value="序号")
    private String seq;

    @ApiModelProperty(value="班次时段开始时间")
    private String shiftStartDate;

    @ApiModelProperty(value="班次时段结束时间")
    private String shiftEndDate;

    @ApiModelProperty(value="是否属于当天， Y： 表示当天 N： 表示前一天 ")
    private String isCurrent;

    @ApiModelProperty(value="说明")
    private String remark;
}
