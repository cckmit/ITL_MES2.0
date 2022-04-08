package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.entity.OperationOrderAndQty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "StemDispatchDTO",description = "线圈工步派工DTO")
public class StemDispatchDTO {
    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "派工单id")
    private String id;

    @ApiModelProperty(value = "车间")
    private String workShop;

    @ApiModelProperty(value = "车间名称")
    private String workshopName;

    @ApiModelProperty(value = "工序bo")
    private String operationBo;

    @ApiModelProperty(value = "工序名称")
    private String operationName;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value = "下达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date releaseDate;

    @ApiModelProperty(value = "工序工单号")
    private String operationOrder;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "工序工单号和数量的集合")
    private List<OperationOrderAndQty> operationOrderAndQtys;

    @ApiModelProperty(value="工步编码")
    private String workStepCode;

    @ApiModelProperty(value = "派工数量")
    private BigDecimal dispatchQty;

    @ApiModelProperty(value = "开始时间")
    private String releaseDateStart;

    @ApiModelProperty(value = "结束时间")
    private String releaseDateEnd;

    @ApiModelProperty(value = "派工单")
    private String stepDispatchCode;

    private List<String> orderBos;

    @ApiModelProperty(value = "用户编码集合")
    private List<String> userIds;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户组id")
    private String roleId;

    private String workReportFlag;

    @ApiModelProperty(value = "重新分派总数")
    private String againDispatchTotalQty;

    @ApiModelProperty(value = "关键字")
    private String keyWord;

    //关键字拆分后的集合
    private List<String> itemDescKeyWordList;
}