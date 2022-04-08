package com.itl.mes.pp.api.dto.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/11/13 14:47
 */

@Data
@ApiModel(value = "排程产线查询实体")
public class ScheduleProductionLineQueryDTO {


    private Page page;

    @ApiModelProperty(value = "车间BO")
    private String workShopBo;


    @ApiModelProperty(value = "排程BO")
    private String scheduleBo;

}
