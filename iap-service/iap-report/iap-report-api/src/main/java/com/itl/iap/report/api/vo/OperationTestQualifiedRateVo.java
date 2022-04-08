package com.itl.iap.report.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OperationTestQualifiedRateVo {
    private Page page;
    private String sfc;
    private String shopOrderBo;
    private String shopOrder;
    private String itemBo;
    private String item;
    private String itemName;
    private String itemDesc;
    private String operationBo;
    private String operation;
    private String operationName;
    private int qty;
    @ApiModelProperty(value = "作业人员")
    private String taskUser;
    @ApiModelProperty(value = "质检人员")
    private String qualityTestingUser;
    @ApiModelProperty(value = "合格率")
    private String qualified;
    @ApiModelProperty(value = "不合格率")
    private String unQualified;
    @ApiModelProperty(value = "不良原因")
    private String ncReason;
    @ApiModelProperty(value = "质检日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date qualityTestingDate;
}
