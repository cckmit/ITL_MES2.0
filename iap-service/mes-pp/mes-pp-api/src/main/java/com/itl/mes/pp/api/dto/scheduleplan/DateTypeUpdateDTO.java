package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DateTypeUpdateDTO",description = "时间类型修改请求对象")
public class DateTypeUpdateDTO {

    @ApiModelProperty(value = "id")
    private String  id;

    @ApiModelProperty(value = "状态，2为休息日，3为节假日")
    private Integer state;


}
