package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OutSourceDto {

    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("车间bo")
    private String workShopBo;

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("设备编码")
    private String device;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

}
