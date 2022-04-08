package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ycw
 */
@Data
@ApiModel(value = "WorkStationDTO",description = "WorkStation查询实体")
public class WorkStationDTO {
    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value="工步编码")
    private String workStepCode;

    @ApiModelProperty(value="工步名称")
    private String workStepName;

    @ApiModelProperty(value="工步描述")
    private String workStepDesc;

    @ApiModelProperty(value="工步所属工序BO")
    private String workingProcess;

    @ApiModelProperty(value="工序编号")
    private String operation;

    @ApiModelProperty(value="工序版本")
    private String operationVersion;

    @ApiModelProperty(value="车间")
    private String workShop;

    private String site;
    private String roleId;



}
