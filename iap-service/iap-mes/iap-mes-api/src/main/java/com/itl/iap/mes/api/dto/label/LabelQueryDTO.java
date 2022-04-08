package com.itl.iap.mes.api.dto.label;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/10/30 14:32
 */
@Data
@ApiModel(value = "标签查询对象")
public class LabelQueryDTO  {

    //分页对象
    private Page page;


    @ApiModelProperty(value = "标签类型Id")
    private String labelTypeId;


    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "工厂编号")
    private String site;


}
