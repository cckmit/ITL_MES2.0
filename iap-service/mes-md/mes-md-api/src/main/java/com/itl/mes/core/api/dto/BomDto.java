package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 崔翀赫
 * @date 2021/1/8$
 * @since JDK1.8
 */
@Data
@ApiModel(value = "BomDto",description = "Bom查询实体")
public class BomDto {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "编码")
    private String bom;
    @ApiModelProperty(value = "版本")
    private String version;
    private String site;
}
