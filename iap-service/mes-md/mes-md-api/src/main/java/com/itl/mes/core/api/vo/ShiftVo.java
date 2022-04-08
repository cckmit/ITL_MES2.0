package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "ShiftVo",description = "保存班次用")
public class ShiftVo implements Serializable {

    @ApiModelProperty(value="SHIFT:SITE,SHIFT【PK-】")
    private String bo;

    @ApiModelProperty(value="工厂【UK-】")
    private String site;

    @ApiModelProperty(value="班次【UK-】")
    private String shift;

    @ApiModelProperty(value="班次名称")
    private String shiftName;

    @ApiModelProperty(value="班次描述")
    private String shiftDesc;

    @ApiModelProperty(value="是否有效 Y：表示当前班次有效 N：表示当前班次无效 ")
    private String isValid;

    @ApiModelProperty(value="班次工作时间，单位为小时")
    private Integer workTime;

    @ApiModelProperty(value="班次生成日期")
    private String productDate;

    @ApiModelProperty(value="班次开始时间")
    private String startTime;

    @ApiModelProperty(value="班次结束时间")
    private String endTime;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value="班次明细集合")
    private List<ShiftDetailVo> shiftDetailVoList;
}
