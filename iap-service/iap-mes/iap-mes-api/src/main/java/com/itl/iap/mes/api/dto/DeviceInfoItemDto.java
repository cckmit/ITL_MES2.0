package com.itl.iap.mes.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Set;

@Data
public class DeviceInfoItemDto {
    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("数据收集组")
    private DataCollectionItem dataCollectionItem;

    @ApiModelProperty("周期类型集合：保养/点检：2 天 1 周 0 季度")
    private Set<String> typeList;
}
