package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.entity.Sfc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "SfcSplitDto",description = "sfc查询实体")
public class SfcSplitDto {


    @ApiModelProperty(value = "编码规则类型")
    String codeRuleType;

    @ApiModelProperty(value="SFC:SITE,SFC【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="SN【UK】")
    private String sfc;

    @ApiModelProperty(value="排程【UK】")
    private String scheduleBo;

    @ApiModelProperty(value="工单")
    private String shopOrderBo;

    @ApiModelProperty(value="车间")
    private String workShopBo;

    @ApiModelProperty(value="产线")
    private String productLineBo;

    @ApiModelProperty(value="当前工序")
    private String operationBo;

    @ApiModelProperty(value="当前工位")
    private String stationBo;

    @ApiModelProperty(value="当前设备")
    private String deviceBo;

    @ApiModelProperty(value="当前工号")
    private String userBo;

    @ApiModelProperty(value="班次")
    private String shitBo;

    @ApiModelProperty(value="班组")
    private String teamBo;

    @ApiModelProperty(value="物料【UK】")
    private String itemBo;

    @ApiModelProperty(value="物料清单")
    private String bomBo;

    @ApiModelProperty(value="SFC工艺路线")
    private String sfcRouterBo;

    @ApiModelProperty(value="SFC工序步骤")
    private String sfcStepId;

    @ApiModelProperty(value="父SFC")
    private String parentSfcBo;

    @ApiModelProperty(value="状态（401新建)")
    private String state;

    @ApiModelProperty(value="当前工序进站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date inTime;

    @ApiModelProperty(value="是否批次处理(false-否,true-是)")
    private String isBatch;

    @ApiModelProperty(value="生产批次")
    private String processLot;

    @ApiModelProperty(value="SFC数量")
    private BigDecimal sfcQty;

    @ApiModelProperty(value="SFC投入数量")
    private BigDecimal inputQty;

    @ApiModelProperty(value="暂停数量")
    private BigDecimal stopQty;

    @ApiModelProperty(value="SFC良品数量")
    private BigDecimal doneQty;

    @ApiModelProperty(value="报废数量")
    private BigDecimal scrapQty;

    @ApiModelProperty(value="修改人")
    private String modifyUser;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date modifyDate;

    @ApiModelProperty(value="工序工单号")
    private String operationOrder;

    @ApiModelProperty(value="不良数量")
    private BigDecimal ncQty;

    @ApiModelProperty(value="物料名称")
    private String itemName;

    @ApiModelProperty(value="物料描述")
    private String itemDesc;

    @ApiModelProperty(value="物料编码")
    private String item;

    @ApiModelProperty(value="工单编码")
    private String shopOrder;

    @ApiModelProperty(value="工序名称")
    private String operationName;

    @ApiModelProperty("记录不良时sfc的工序流程ID（不良拆分时使用）")
    private String repairOpIds;

    @ApiModelProperty(value="入库标签拆分数量")
    private int inSfcQty;

    @ApiModelProperty("派工单编码")
    private String dispatchCode;
}
