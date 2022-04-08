package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "PlainShiftVo",description = "车间日历中查询班次用")
public class PlainShiftVo implements Serializable{
    @ApiModelProperty(value = "班次")
    private String shift;
    @ApiModelProperty(value = "班次名称")
    private String shiftName;
}
