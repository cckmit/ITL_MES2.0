package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.regex.Pattern;

@Data
@ApiModel(value = "MouldDto",description = "模具查询实体")
public class MouldDto {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "模具编码")
    private String mould;

    @ApiModelProperty(value = "产品(物料)名称")
    private String itemName;

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("工序名称")
    private String operationName;

    @ApiModelProperty("状态（0待归还，1已完成）")
    private String state;

    @ApiModelProperty("领用人员")
    private String collectUser;

    @ApiModelProperty("归还人员")
    private String returnUser;

    @ApiModelProperty("开始时间")
    @JsonFormat(
        pattern = "yyyy-MM-dd",
        timezone = "GMT+8"
    )
    private Date startDate;

    @ApiModelProperty("结束时间")
    @JsonFormat(
        pattern = "yyyy-MM-dd",
        timezone = "GMT+8"
    )
    private Date endDate;



}
