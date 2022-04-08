package com.itl.iap.mes.api.dto.parameter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/11/4 11:23
 */
@Data
@ApiModel(value = "参数请求实体")
public class ParameterQueryDTO {


    //分页对象
    private Page page;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;


}
