package com.itl.iap.mes.api.vo;

import com.itl.iap.mes.api.entity.DataCollectionItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

@Data
public class DeviceInfoItemVo {

    @ApiModelProperty("保养/点检：2 天 1 周 0 季度")
    private String type;

    @ApiModelProperty("数据收集组集合")
    private List<DataCollectionItem> dataCollectionItems;

    @ApiModelProperty("分页信息")
    private Page page;



}
