package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "DateTypeQueryDTO",description = "时间类型查询请求对象")
public class DateTypeQueryDTO {


    @ApiModelProperty(value = "年")
    @NotNull
    private Integer year;

    @ApiModelProperty(value = "月")
    @NotNull
    private Integer month;


}
