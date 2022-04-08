package com.itl.mes.pp.api.dto.scheduleplan;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @User liuchenghao
 * @Date 2020/11/25 10:13
 */
@Data
@ApiModel(value = "DateTypeSaveDTO",description = "日期类型保存请求实体")
public class DateTypeSaveDTO {

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;

}
