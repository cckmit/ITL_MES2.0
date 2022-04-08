package com.itl.iap.mes.api.dto.toolgroup;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/11/6 14:21
 */
@Data
@ApiModel(value = "工具组查询请求实体")
public class ToolGroupQueryDTO {

    @ApiModelProperty(value = "分页对象")
    private Page page;


    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "工具组")
    private String toolGroup;


    @ApiModelProperty(value = "状态")
    private String state;


    @ApiModelProperty(value = "工具组描述")
    private String tgDesc;


}
