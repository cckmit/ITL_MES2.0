package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sky,
 * @date 2019/8/30
 * @time 16:28
 */
@Data
@ApiModel(value = "InspectTaskVo", description = "检验任务使用")
public class InspectTaskVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="检验任务")
    private String inspectTask;

    @ApiModelProperty(value="检验类型")
    @Excel( name="检验类型", orderNum="0" )
    private String inspectType;

    @ApiModelProperty(value="车间")
    @Excel( name="车间", orderNum="1" )
    private String workShop;

    @ApiModelProperty(value="产线")
    @Excel( name="产线", orderNum="2" )
    private String productLine;

    @ApiModelProperty(value="工序")
    @Excel( name="工序", orderNum="3" )
    private String operation;

    @ApiModelProperty(value="物料")
    @Excel( name="物料", orderNum="4" )
    private String item;

    @ApiModelProperty(value="工单")
    @Excel( name="工单", orderNum="5" )
    private String shopOrder;

    @ApiModelProperty(value="条码")
    @Excel( name="条码", orderNum="6" )
    private String sn;

    @ApiModelProperty(value="任务状态(0:新建，1：进行中，2:完成，3：关闭，4：删除)")
    private String state;

    /*@ApiModelProperty(value="质量控制计划BO")
    private String qualityPlanBo;*/

    /*@ApiModelProperty(value="创建方式（A：自动，M：手动）")
    @Length( max = 10 )
    @TableField("CREATE_METHOD")
    private String createMethod;*/

    @ApiModelProperty(value="修改日期")
    private Date modifyDate;


}
