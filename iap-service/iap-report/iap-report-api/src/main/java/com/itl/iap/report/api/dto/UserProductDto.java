package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserProductDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("账号")
    private String userName;

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;
}
