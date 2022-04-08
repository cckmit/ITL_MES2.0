package com.itl.mes.core.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户工号")
    private String userBo;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "工序bo")
    private String operationBo;

    @ApiModelProperty(value = "工序")
    private String operation;

    @ApiModelProperty(value = "工序名")
    private String operationName;

    @ApiModelProperty(value = "工位bo")
    private String stationBo;

    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "工位名")
    private String stationName;

    @ApiModelProperty(value = "工序类型 1:检验工序 0:过站工序")
    private int operationType;

    private String workShop;
    private String workShopDesc;
    private String workShopBo;

    @ApiModelProperty("产线名称")
    private String productLineName;

    @ApiModelProperty(value = "工步Bo")
    private String workStationBo;
    @ApiModelProperty(value = "1代表需要自动报工,0代表不需要")
    private String isAllStepReportWork;
    private String productLineBo;

}
