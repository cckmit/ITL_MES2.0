package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 崔翀赫
 * @date 2020/12/25$
 * @since JDK1.8
 */
@Data
@ApiModel("SN日志dto")
public class SnLogDto {
    @ApiModelProperty(value = "开始号段")
    private String startNumber;

    @ApiModelProperty(value = "结束号段")
    private String endNumber;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "分页对象")
    private Page page;

}
