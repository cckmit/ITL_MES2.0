package com.itl.iap.mes.api.dto.demand;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/5 16:06
 */
@Data
@ApiModel(value = "需求查询实体")
public class DemandQueryDTO {

    //分页对象
    private Page page;


    @ApiModelProperty(value = "需求号")
    private String demandNo;

    @ApiModelProperty(value = "需求类型")
    private String demandType;

    @ApiModelProperty(value = "客户编号")
    private String customerBo;

    @ApiModelProperty(value = "是否删除，默认不传")
    private Integer isDelete;

    @ApiModelProperty(value = "物料编号")
    private String materialNo;

    @ApiModelProperty(value = "创建开始时间")
    private Date createStartDate;

    @ApiModelProperty(value = "创建结束时间")
    private Date createEndDate;

    @ApiModelProperty(value = "交付开始时间")
    private Date planStartDate;


    @ApiModelProperty(value = "交付结束时间")
    private Date planEndDate;



}
