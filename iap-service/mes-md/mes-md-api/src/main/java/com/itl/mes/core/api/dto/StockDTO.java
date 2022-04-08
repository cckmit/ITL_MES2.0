package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.plugin.Signature;


@Data
public class StockDTO {
    //分页对象
    private Page page;

    @ApiModelProperty(value = "批次")
    private String sfc;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "仓库")
    private String wareHouse;

    @ApiModelProperty(value = "入库申请单")
    private String bo;

    @ApiModelProperty(value = "入库成功标识（0，失败 1，成功）")
    private String successFlag;
}
