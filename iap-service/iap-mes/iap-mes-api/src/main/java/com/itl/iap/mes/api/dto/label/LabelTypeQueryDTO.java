package com.itl.iap.mes.api.dto.label;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/10/30 10:31
 */
@ApiModel(value = "标签类型查询对象")
@Data
public class LabelTypeQueryDTO {


    //分页对象
    private Page page;

    @ApiModelProperty(value = "标签类型")
    private String labelType;


    @ApiModelProperty(value = "工厂编号")
    private String site;

}
