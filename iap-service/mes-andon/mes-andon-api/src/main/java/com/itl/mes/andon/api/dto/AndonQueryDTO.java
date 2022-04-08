package com.itl.mes.andon.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AndonQueryDTO",description = "安灯查询请求实体")
public class AndonQueryDTO {

    @ApiModelProperty(value = "分页对象")
    private Page page;

    @ApiModelProperty(value = "安灯编码")
    private String andon;


    @ApiModelProperty(value = "安灯名称")
    private String andonName;


    @ApiModelProperty(value = "是否启用，启用为true,停用为false")
    private Boolean isEnable;

}
