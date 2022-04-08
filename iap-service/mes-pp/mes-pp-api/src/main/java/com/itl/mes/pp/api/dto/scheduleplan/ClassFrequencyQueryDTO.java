package com.itl.mes.pp.api.dto.scheduleplan;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassFrequencyQueryDTO",description = "班次查询请求对象")
public class ClassFrequencyQueryDTO {

    @ApiModelProperty(value = "分页对象")
    private Page page;


    @ApiModelProperty(value = "班制ID")
    private String classSystemId;


}
