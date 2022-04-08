package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/10/28 14:23
 */

@Data
@ApiModel(value = "StationProveVo",description = "保存工位证明关联数据")
public class StationProveVo {

    @ApiModelProperty(value="工位编号")
    private String stationBo;

    @ApiModelProperty(value="证明ID集合")
    private List<String> proveIds;

}
