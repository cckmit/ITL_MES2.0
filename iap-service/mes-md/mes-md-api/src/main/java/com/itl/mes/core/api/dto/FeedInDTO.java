package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "FeedInDTO",description = "上料查询实体")
public class FeedInDTO {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "设备Bo")
    private String deviceBo;

    @ApiModelProperty(value = "工单Bo")
    private String shopOrderBo;

    @ApiModelProperty(value = "sfc")
    private String sfc;
}
