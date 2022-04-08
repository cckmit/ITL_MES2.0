package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("工序产能DTO")
public class OperationCapacityDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("车间BO")
    private String workShopBo;

    @ApiModelProperty("工序BO")
    private String operationBo;

    @ApiModelProperty("物料BO")
    private String itemBo;

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date startTime;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date endTime;
}
