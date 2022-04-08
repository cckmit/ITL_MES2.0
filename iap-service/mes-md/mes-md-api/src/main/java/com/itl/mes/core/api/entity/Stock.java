package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("m_dy_stock")
@ApiModel(value="stock",description="入库信息表")
public class Stock extends Model<Stock> {
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }

    @ApiModelProperty(value="STOCK:SITE,STOCK【PK-】")
    @Length( max = 255 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value = "工厂")
    @Length(max = 255)
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value = "批次")
    @Length(max = 255)
    @TableField("SFC")
    private String sfc;

    @ApiModelProperty(value = "库")
    @Length(max = 255)
    @TableField("WAREHOUSE")
    private String warehouse;

    @ApiModelProperty(value = "良品数")
    @Length(max = 255)
    @TableField("OK_QTY")
    private int okQty;

    @ApiModelProperty(value = "报废数")
    @Length(max = 255)
    @TableField("SCRAP")
    private int scrap;

    @ApiModelProperty(value = "工单bo")
    @Length(max = 255)
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value = "物料bo")
    @Length(max = 255)
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value = "检验人")
    @Length(max = 255)
    @TableField("CHECK_USER")
    private String checkUser;

    @ApiModelProperty(value = "检验人名")
    @Length(max = 255)
    @TableField("CHECK_USER_NAME")
    private String checkUserName;

    @ApiModelProperty(value = "申请人")
    @Length(max = 255)
    @TableField("APPLY_USER")
    private String applyUser;

    @ApiModelProperty(value = "申请人名")
    @Length(max = 255)
    @TableField("APPLY_USER_NAME")
    private String applyUserName;

    @ApiModelProperty(value = "工单")
    @TableField(exist = false)
    private String shopOrder;

    @ApiModelProperty(value = "工单")
    @TableField(exist = false)
    private String item;

    @ApiModelProperty(value = "sfc数量")
    @TableField(exist = false)
    private BigDecimal sfcQty;

    @ApiModelProperty(value = "差异数")
    @TableField("DIF_QTY")
    private BigDecimal difQty;

    @ApiModelProperty(value = "sfc数量")
    @TableField("SFC_QTY")
    private BigDecimal sfcTotalQty;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value = "成功标识（0:入库失败 1:入库成功）")
    @TableField("SUCCESS_FLAG")
    private String successFlag;

    @ApiModelProperty(value = "失败原因")
    @TableField("FAILED_REASON")
    private String failedReason;
    
    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String SFC = "SFC";

    public static final String WAREHOUSE = "WAREHOUSE";

    public static final String OK_QTY = "OK_QTY";

    public static final String SCRAP = "SCRAP";

    public static final String IN_STOCK_FAILED = "0";
}
