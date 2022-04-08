package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.report.api.vo.AndonExceptionVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AndonExceptionDto {

    @ApiModelProperty("安灯类型名称")
    private List<String> andonTypeNames;

    @ApiModelProperty("未签到数")
    private List<Integer> signNums;

    @ApiModelProperty("处理中数")
    private List<Integer> runNums;

    @ApiModelProperty("已解除数")
    private List<Integer> finishedNums;

    @ApiModelProperty("安灯异常集合")
    private IPage<AndonExceptionVo> andonExceptionVoList;
}
