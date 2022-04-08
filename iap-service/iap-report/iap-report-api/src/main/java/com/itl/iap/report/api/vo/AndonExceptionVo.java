package com.itl.iap.report.api.vo;

import com.itl.iap.report.api.entity.AndonException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AndonExceptionVo {
    @ApiModelProperty("车间")
    private String workShopName;

    @ApiModelProperty("安灯类型")
    private String andonTypeName;

    @ApiModelProperty("已解除")
    private Integer finishedNum;

    @ApiModelProperty("处理中")
    private Integer runNum;

    @ApiModelProperty("未签到")
    private Integer signNum;

    @ApiModelProperty("平均相应时间")
    private long responseTime;

    @ApiModelProperty("平均处理用时")
    private long handleTime;

    @ApiModelProperty("未签到集合")
    List<AndonException> signList;

    @ApiModelProperty("处理中集合")
    List<AndonException> handleList;

    @ApiModelProperty("已解除集合")
    List<AndonException> finishedList;
}
