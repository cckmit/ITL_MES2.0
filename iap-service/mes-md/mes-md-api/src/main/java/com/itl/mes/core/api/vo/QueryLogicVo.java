package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@ApiModel(value = "QueryLogicVo", description = "查询数据")
public class QueryLogicVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="逻辑编号")
    private String logicNo;

    @ApiModelProperty(value="版本，可为空")
    private String version;

    @ApiModelProperty(value="参数")
    private Map<String,Object> params;
}
