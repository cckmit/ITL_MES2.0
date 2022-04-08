package com.itl.iap.report.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("me_sfc")
@ApiModel(value="Sfc",description="批次条码表")
public class Sfc extends Model<Sfc> {

    private static final long serialVersionUID = -8384486979728802613L;

    @ApiModelProperty(value="SFC:SITE,SFC【PK】")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="SN【UK】")
    @Length( max = 32 )
    @TableField("SFC")
    private String sfc;

    @ApiModelProperty(value="排程【UK】")
    @Length( max = 50 )
    @TableField("SCHEDULE_BO")
    private String scheduleBo;

    @ApiModelProperty(value="工单")
    @Length( max = 64 )
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value="车间")
    @Length( max = 32 )
    @TableField("WORK_SHOP_BO")
    private String workShopBo;

    @ApiModelProperty(value="产线")
    @Length( max = 100 )
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;

    @ApiModelProperty(value="当前工序")
    @Length( max = 100 )
    @TableField("OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value="当前工位")
    @Length( max = 200 )
    @TableField("STATION_BO")
    private String stationBo;

    @ApiModelProperty(value="当前设备")
    @Length( max = 100 )
    @TableField("DEVICE_BO")
    private String deviceBo;

    @ApiModelProperty(value="当前工号")
    @Length( max = 100 )
    @TableField("USER_BO")
    private String userBo;

    @ApiModelProperty(value="班次")
    @Length( max = 100 )
    @TableField("SHIT_BO")
    private String shitBo;

    @ApiModelProperty(value="班组")
    @Length( max = 100 )
    @TableField("TEAM_BO")
    private String teamBo;

    @ApiModelProperty(value="物料【UK】")
    @Length( max = 100 )
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="物料清单")
    @Length( max = 100 )
    @TableField("BOM_BO")
    private String bomBo;

    @ApiModelProperty(value="SFC工艺路线")
    @Length( max = 100 )
    @TableField("SFC_ROUTER_BO")
    private String sfcRouterBo;

    @ApiModelProperty(value="SFC工序步骤")
    @Length( max = 100 )
    @TableField("SFC_STEP_ID")
    private String sfcStepId;

    @ApiModelProperty(value="父SFC")
    @Length( max = 64 )
    @TableField("PARENT_SFC_BO")
    private String parentSfcBo;

    @ApiModelProperty(value="状态（401新建)")
    @Length( max = 100 )
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value="当前工序进站时间")
    @TableField("IN_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date inTime;

    @ApiModelProperty(value="是否批次处理(false-否,true-是)")
    @TableField("IS_BATCH")
    private String isBatch;

    @ApiModelProperty(value="生产批次")
    @Length( max = 100 )
    @TableField("PROCESS_LOT")
    private String processLot;

    @ApiModelProperty(value="SFC数量")
    @Length( max = 18 )
    @TableField("SFC_QTY")
    private BigDecimal sfcQty;

    @ApiModelProperty(value="SFC投入数量")
    @Length( max = 18 )
    @TableField("INPUT_QTY")
    private BigDecimal inputQty;

    @ApiModelProperty(value="SFC良品数量")
    @Length( max = 18 )
    @TableField("DONE_QTY")
    private BigDecimal doneQty;

    @ApiModelProperty(value="报废数量")
    @Length( max = 18 )
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty;

    @ApiModelProperty(value="修改人")
    @Length( max = 30 )
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value="修改时间")
    @TableField("MODIFY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date modifyDate;

    @ApiModelProperty("创建时间")
    @TableField("CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value="工序工单号")
    @Length( max = 255 )
    @TableField("OPERATION_ORDER")
    private String operationOrder;

    @ApiModelProperty(value="不良数量")
    @TableField("NC_QTY")
    private String ncQty;

    @ApiModelProperty(value="物料名称")
    @TableField(exist = false)
    private String itemName;

    @ApiModelProperty(value="物料描述")
    @TableField(exist = false)
    private String itemDesc;

    @ApiModelProperty(value="物料编码")
    @TableField(exist = false)
    private String item;

    @ApiModelProperty(value="工单编码")
    @TableField(exist = false)
    private String shopOrder;

    @ApiModelProperty(value="工序名称")
    @TableField(exist = false)
    private String operationName;

    @ApiModelProperty(value="工序名称")
    @TableField("dispatch_code")
    private String dispatchCode;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
