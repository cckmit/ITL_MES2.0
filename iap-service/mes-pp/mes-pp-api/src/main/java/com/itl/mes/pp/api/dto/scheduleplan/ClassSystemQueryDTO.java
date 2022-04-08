package com.itl.mes.pp.api.dto.scheduleplan;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassSystemQueryDTO",description = "班制查询请求实体")
public class ClassSystemQueryDTO {

    @ApiModelProperty(value = "分页对象")
    private Page page;


    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

}
