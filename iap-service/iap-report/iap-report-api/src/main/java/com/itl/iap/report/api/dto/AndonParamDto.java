package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AndonParamDto {
    @ApiModelProperty("分页")
    private Page page;

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("车间")
    private String workShopBo;

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("设备编码")
    private String device;

    @ApiModelProperty("异常类型")
    private String andonTypeName;

    @ApiModelProperty("触发人员")
    private String triggerUser;

}
