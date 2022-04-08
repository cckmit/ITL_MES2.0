package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "WaitingDocumentsVO",description = "待检单据")
public class WaitingDocumentsVO {

    @ApiModelProperty(value="检验类型（0:首件检验，1:巡检检验，2:最终检验）")
    private String checkType;

    @ApiModelProperty(value="检验单编号")
    private String checkCode;

    @ApiModelProperty(value="批次条码")
    private String sfc;

    @ApiModelProperty(value="自检时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm",
            timezone = "GMT+8"
    )
    private Date selfCheckDate;

    @ApiModelProperty(value="品检时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm",
            timezone = "GMT+8"
    )
    private Date qualityCheckDate;

    @ApiModelProperty(value="等待时间")
    private long waitingTime;
}
