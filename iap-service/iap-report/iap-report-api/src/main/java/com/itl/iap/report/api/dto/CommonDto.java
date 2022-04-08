package com.itl.iap.report.api.dto;

import com.itl.iap.report.api.vo.CommonVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class CommonDto {
    @ApiModelProperty("值")
    private String name;

    @ApiModelProperty("集合")
    private List<CommonVo> commonVoList;
}
