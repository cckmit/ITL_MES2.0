package com.itl.iap.report.api.dto;

import com.itl.iap.report.api.vo.AndonVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AndonDto {
    @ApiModelProperty("安灯信息统计")
    private List<AndonVo> andonInfo;

    @ApiModelProperty("Andon类型次数分布图")
    private List<AndonVo> andonTypes;

    @ApiModelProperty("Andon类型持续时间分布图")
    private List<AndonVo> andonTimes;

    @ApiModelProperty("车间Andon异常统计图")
    private List<AndonVo> andonWorkShopNames;

    @ApiModelProperty("Andon异常次数月汇总")
    private AndonVo andonSumMonth;

    @ApiModelProperty("Andon异常次数周汇总")
    private AndonVo andonSumWeek;

    @ApiModelProperty("Andon异常次数日汇总")
    private AndonVo andonSumDay;





}
