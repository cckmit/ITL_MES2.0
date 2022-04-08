package com.itl.iap.report.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("员工日产量Vo")
public class StaffDailyEnergyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Page page;
    private String workShopBo;
    private String workShopName;
    private String workShop;
    private String itemBo;
    private String item;
    private String itemDesc;
    private String itemName;
    private String userBo;
    private String userName;
    private String operationBo;
    private String operationName;
    private String operation;
    private String workStepCodeBo;
    private String workStepName;
    private BigDecimal qty;

    private String startTime;
    private String endTime;

    @ApiModelProperty("加工时间")
    private String machiningDate;
}
