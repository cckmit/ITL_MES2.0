package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工序查询")
public class OperationDTO {

    //分页对象
    private Page page;

    private String sfc;

    private String checkType;

    private String operationBo;

    private String operation;

    private String operationName;

    private String checkCode;

    @ApiModelProperty("工单编码")
    private String shopOrder;

    @ApiModelProperty("设备编码")
    private String device;
}
