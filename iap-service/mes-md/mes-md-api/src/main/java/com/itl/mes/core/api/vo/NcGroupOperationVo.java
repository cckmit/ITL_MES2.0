package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "NcGroupOperationVo",description = "不合格代码组中返回工序用")
public class NcGroupOperationVo implements Serializable {

    @ApiModelProperty(value="工序编号【UK】")
    private String operation;

    @ApiModelProperty(value="版本")
    private String version;

    @ApiModelProperty(value="工序名称")
    private String operationName;

    @ApiModelProperty(value="描述")
    private String operationDesc;

}
