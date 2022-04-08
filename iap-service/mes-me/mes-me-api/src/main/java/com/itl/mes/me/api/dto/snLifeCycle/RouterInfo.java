package com.itl.mes.me.api.dto.snLifeCycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2021/1/29
 * @since JDK1.8
 */
@Data
@ApiModel("SN-Router")
public class RouterInfo {
    @ApiModelProperty("工艺路线名")
    private String routerName;
    @ApiModelProperty("工艺路线版本")
    private String routerVersion;
    @ApiModelProperty("工艺路线状态")
    private String routerState;
    @ApiModelProperty("当前工序")
    private String currentOperation;
    @ApiModelProperty("当前工步状态")
    private String currentOperationState;
    @ApiModelProperty("下一工序")
    private String nextOperation;
}
