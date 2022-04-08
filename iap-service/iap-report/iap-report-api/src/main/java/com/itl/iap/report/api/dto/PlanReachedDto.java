package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class PlanReachedDto {
    private Page page;
    private String workShopBo;
    private String workShop;
    private String itemBo;
    private String planStartTimeStart;
    private String planStartTimeEnd;
    private String planEndTimeStart;
    private String planEndTimeEnd;

    private String shopOrderBo;
}
