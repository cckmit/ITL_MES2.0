package com.itl.mes.core.api.vo;

import com.itl.mes.core.api.entity.QualityPlanParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "OperationParamsVo",description = "工序检验项")
public class OperationParamsVo implements Serializable {

    @ApiModelProperty(value="工序名称")
    private String operationName;

    @ApiModelProperty(value="工序BO")
    private String operationBo;

    @ApiModelProperty(value="是否非必填")
    private String isMust;

    @ApiModelProperty(value = "检验单编号集合")
    private List<String> checkCode;

    @ApiModelProperty(value="检验项")
    private List<QualityPlanParameter> parameters;
}
