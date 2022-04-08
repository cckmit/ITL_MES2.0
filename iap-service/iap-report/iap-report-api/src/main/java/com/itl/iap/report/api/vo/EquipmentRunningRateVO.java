package com.itl.iap.report.api.vo;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;


@Data
@Accessors(chain = true)
public class EquipmentRunningRateVO {

    Page page;

    @ApiModelProperty("设备编号")
    private String device;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备描述")
    private String deviceDesc;

    @ApiModelProperty("车间")
    private String workShopDesc;

    @ApiModelProperty("产线")
    private String productLineDesc;

    @ApiModelProperty("状态-分组")
    private String fMachState;

    @ApiModelProperty("时间-用于聚合")
    private Double continuedTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("时间-用于筛选")
    private Date createDate;

    @ApiModelProperty("运行时长-需计算")
    private Double operationTime;

    @ApiModelProperty("待机时长-需计算")
    private Double standbyTime;

    @ApiModelProperty("报警时长-需计算")
    private Double riskTime;

    @ApiModelProperty("关机或断网时长-需计算")
    private Double closeTime;

    @ApiModelProperty("调试时长-需计算")
    private Double debugTime;

    @ApiModelProperty("时间稼动率-需计算")
    private Double timeRate;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date endTime;


}
