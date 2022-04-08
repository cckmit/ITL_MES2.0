package com.itl.mes.andon.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "安灯明细查询对象")
public class AndonDetailDTO {

    //分页对象
    private Page page;

    @ApiModelProperty(value = "安灯异常明细")
    private String andonDesc;

    @ApiModelProperty(value = "安灯类型名称")
    private String andonName;
}
