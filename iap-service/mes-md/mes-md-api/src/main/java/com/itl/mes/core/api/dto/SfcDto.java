package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "SfcDto",description = "Sfc查询")
public class SfcDto {
    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "工序工单号")
    private String operationOrder;

    @ApiModelProperty(value = "工单号")
    private String shopOrder;

    @ApiModelProperty(value = "生成日期")
    private String scDate;

    @ApiModelProperty(value = "结束日期")
    private String jsDate;

    @ApiModelProperty(value = "sfc条码")
    private String sfc;

    @ApiModelProperty(value = "设备bo集合字符串（一个字符串，中间用 | 隔开）")
    private String deviceBos;

    @ApiModelProperty(value = "进站数量")
    private BigDecimal inputQty;

    @ApiModelProperty(value = "设备bo集合")
    private List<String> deviceList;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("工位bo")
    private String stationBo;

    @ApiModelProperty("首工序")
    private String operationBo;

    @ApiModelProperty("工单BO")
    private String shopOrderBo;

    @ApiModelProperty("物料编码")
    private String item;

    @ApiModelProperty("物料规格")
    private String itemDesc;

    @ApiModelProperty("车间")
    private String workShop;

    @ApiModelProperty("派工单编码")
    private String dispatchCode;

    @ApiModelProperty(value = "sfc数量")
    private BigDecimal sfcQty;
}
