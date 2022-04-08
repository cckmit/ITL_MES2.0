package com.itl.mes.andon.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2020/12/18
 * @since JDK1.8
 */
@Data
@ApiModel(value = "Box灯箱查询实体")
public class BoxQueryVo {


    @ApiModelProperty(value = "分页对象")
    private Page page;


    @ApiModelProperty(value = "灯箱编号")
    private String box;


    @ApiModelProperty(value = "灯箱名称")
    private String boxName;


    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "工厂")
    private String site;
}
