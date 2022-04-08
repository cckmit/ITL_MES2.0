package com.itl.iap.mes.api.dto.toolgroup;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/11/6 14:23
 */
@Data
@ApiModel(value = "工具组附加对象查询请求实体")
public class ToolGroupAttachQueryDTO {


    @ApiModelProperty(value = "分页对象")
    private Page page;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "工具组BO")
    private String toolGroupBo;

}
