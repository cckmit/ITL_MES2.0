package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2020/12/18
 */
@Data
@ApiModel(value = "UserStationQueryDTO",description = "用户工位查询实体")
public class UserStationQueryDTO {

    @ApiModelProperty(value = "分页信息")
    private Page page;


    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "工位编号")
    private String station;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "车间BO")
    private String workShopBo;


}
