package com.itl.mes.andon.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
@Data
@ApiModel(value = "AndonTriggerAndonVo",description = "")
public class AndonTriggerAndonVo{

    @ApiModelProperty(value = "安灯BO")
    private String andonBo;

    @ApiModelProperty(value = "安灯名称")
    private String andonName;

    @ApiModelProperty(value = "资源类型，0代表未绑定资源，4代表设备，6代表物料，7代表质量")
    private String resourceType;

    @ApiModelProperty(value = "物料BO")
    private String itemBo;

    @ApiModelProperty(value = "设备BO")
    private String deviceBo;

    @ApiModelProperty(value = "状态,1代表异常，2代表正常")
    private String state;


}
