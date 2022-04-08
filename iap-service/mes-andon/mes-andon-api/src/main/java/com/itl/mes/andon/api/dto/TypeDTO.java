package com.itl.mes.andon.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "andon类型")
public class TypeDTO {

    private Page page;
    /** 安灯类型 */
    @ApiModelProperty(value = "安灯类型")
    private String andonType;
    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String andonTypeName;
}
