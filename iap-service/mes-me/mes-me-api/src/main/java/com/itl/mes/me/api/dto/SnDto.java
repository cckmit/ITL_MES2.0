package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 崔翀赫
 * @date 2020/12/29$
 * @since JDK1.8
 */
@Data
@ApiModel("条码号段领用登记")
public class SnDto {

    @ApiModelProperty(value = "开始条码")
    private String startSn;

    @ApiModelProperty(value = "结束条码")
    private String endSn;


    @ApiModelProperty(value = "领用人")
    private String getUser;

    @ApiModelProperty(value = "产线")
    private String productLine;

    @ApiModelProperty(value = "车间")
    private String workShop;


    @ApiModelProperty(value = "分页对象")
    private Page page;
}
