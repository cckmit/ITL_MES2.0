package com.itl.mes.andon.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/25
 */
@Data
@ApiModel(value = "AndonLookPlateVo",description = "安灯看板返回实体")
public class AndonLookPlateVo {



    @ApiModelProperty(value = "资源名称")
    private String resourceName;


    @ApiModelProperty(value = "是否异常,true表示异常，false表示正常")
    private Boolean isAbnormal;


    @ApiModelProperty(value = "设备异常")
    private List<String> deviceAbnormal;

    @ApiModelProperty(value = "物料异常")
    private List<String> itemAbnormal;

    @ApiModelProperty(value = "质量异常")
    private List<String> qualityAbnormal;


}
