package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "OperationOrderDTO",description = "工序工单Dto")
public class OperationOrderDTO {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "车间")
    private String workShop;

    @ApiModelProperty(value = "工序Bo")
    private String operationBo;

    @ApiModelProperty(value = "工序Bos")
    private List<String> operationBos;

    @ApiModelProperty(value = "工序工单号")
    private String operationOrder;

    @ApiModelProperty(value = "工序")
    private String operation;

    @ApiModelProperty(value = "工序")
    private String itemDesc;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "车间名称")
    private String workShopName;

    @ApiModelProperty(value = "如果是转轴车间设置为1")
    private String flag;

    public static final String WORK_SHOP_NAME_ = "转轴车间";

    public static final String FLAG_ = "1";
}
