package com.itl.iap.mes.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.entity.CheckPlan;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "CheckPlanVo",description = "设备点检计划返回实体")
public class CheckPlanVo {

    @ApiModelProperty(value = "设备点检计划")
    CheckPlan checkPlan;

    @ApiModelProperty(value = "数据收集组信息")
    private List<DataCollectionItem> dataCollectionItems;

    @ApiModelProperty(value = "设备点检单编号")
    private String deviceCheckCode;

    @ApiModelProperty(value = "点检人")
    private String checkPerson;

    @ApiModelProperty("点检时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date checkTime;



}
