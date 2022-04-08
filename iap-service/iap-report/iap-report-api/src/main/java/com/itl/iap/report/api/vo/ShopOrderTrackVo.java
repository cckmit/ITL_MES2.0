package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ShopOrderTrackVo {

    @ApiModelProperty("当前页")
    private long current;

    @ApiModelProperty("页数")
    private long pages;

    @ApiModelProperty("个数")
    private long size;

    @ApiModelProperty("总个数")
    private long total;

    @ApiModelProperty("最大数量")
    private int maxNum;

    @ApiModelProperty("数据集合")
    private List<Map<String,Object>> shopOrderTrackList;

    @ApiModelProperty("工单集合")
    private Set<String> shopOrders;
}
