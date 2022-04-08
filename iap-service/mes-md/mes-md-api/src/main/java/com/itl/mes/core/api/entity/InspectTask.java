package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 检验任务
 * </p>
 *
 * @author space
 * @since 2019-08-30
 */
@TableName("z_inspect_task")
@ApiModel(value="InspectTask",description="检验任务")
public class InspectTask extends Model<InspectTask> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="InspectTaskBO:SITE,INSPECT_TASK")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="检验任务")
    @Length( max = 64 )
    @TableField("INSPECT_TASK")
    @Excel( name="检验任务编号", orderNum="0" )
    private String inspectTask;

    @ApiModelProperty(value="检验类型")
    @Length( max = 64 )
    @TableField("INSPECT_TYPE")
    @Excel( name="检验类型", orderNum="1" )
    private String inspectType;

    @ApiModelProperty(value="检验结果(PASS/FAIL)")
    @Length( max = 64 )
    @TableField("INSPECT_RESULT")
    private String inspectResult;

    @ApiModelProperty(value="车间")
    @Length( max = 64 )
    @TableField("WORK_SHOP")
    @Excel( name="车间", orderNum="2" )
    private String workShop;

    @ApiModelProperty(value="产线")
    @Length( max = 64 )
    @TableField("PRODUCT_LINE")
    @Excel( name="线位", orderNum="3" )
    private String productLine;

    @ApiModelProperty(value="工序")
    @Length( max = 64 )
    @TableField("OPERATION")
    @Excel( name="工序", orderNum="5" )
    private String operation;

    @ApiModelProperty(value="物料")
    @Length( max = 64 )
    @TableField("ITEM")
    @Excel( name="物料", orderNum="4" )
    private String item;

    @ApiModelProperty(value="工单")
    @Length( max = 64 )
    @TableField("SHOP_ORDER")
    @Excel( name="工单", orderNum="6" )
    private String shopOrder;

    @ApiModelProperty(value="条码")
    @Length( max = 64 )
    @TableField("SN")
    @Excel( name="产品条码", orderNum="7" )
    private String sn;

    @ApiModelProperty(value="任务状态(0:新建，1：进行中，2:完成，3：关闭，4：删除)")
    @Length( max = 10 )
    @TableField("STATE")
    @Excel( name="任务状态", orderNum="8" )
    private String state;

    @ApiModelProperty(value="质量控制计划BO")
    @Length( max = 100 )
    @TableField("QUALITY_PLAN_BO")
    private String qualityPlanBo;

    @ApiModelProperty(value="创建方式（A：自动，M：手动）")
    @Length( max = 10 )
    @TableField("CREATE_METHOD")
    private String createMethod;

    @ApiModelProperty(value="建档人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    @Excel( name="创建人", orderNum="10" )
    private String createUser;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
    @Excel( name="创建时间", orderNum="9", exportFormat = "yyyy-MM-dd HH:mm:ss",importFormat = "yyyy-MM-dd HH:mm:ss" )
    private Date createDate;

    @ApiModelProperty(value="修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("MODIFY_USER")
    private String modifyUser;


    public String getBo() {
      return bo;
   }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getSite() {
      return site;
   }

    public void setSite(String site) {
        this.site = site;
    }

    public String getInspectTask() {
      return inspectTask;
   }

    public void setInspectTask(String inspectTask) {
        this.inspectTask = inspectTask;
    }

    public String getInspectType() {
      return inspectType;
   }

    public void setInspectType(String inspectType) {
        this.inspectType = inspectType;
    }

    public String getInspectResult() {
      return inspectResult;
   }

    public void setInspectResult(String inspectResult) {
        this.inspectResult = inspectResult;
    }

    public String getWorkShop() {
      return workShop;
   }

    public void setWorkShop(String workShop) {
        this.workShop = workShop;
    }

    public String getProductLine() {
      return productLine;
   }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getOperation() {
      return operation;
   }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getItem() {
      return item;
   }

    public void setItem(String item) {
        this.item = item;
    }

    public String getShopOrder() {
      return shopOrder;
   }

    public void setShopOrder(String shopOrder) {
        this.shopOrder = shopOrder;
    }

    public String getSn() {
      return sn;
   }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getState() {
      return state;
   }

    public void setState(String state) {
        this.state = state;
    }

    public String getQualityPlanBo() {
      return qualityPlanBo;
   }

    public void setQualityPlanBo(String qualityPlanBo) {
        this.qualityPlanBo = qualityPlanBo;
    }

    public String getCreateMethod() {
      return createMethod;
   }

    public void setCreateMethod(String createMethod) {
        this.createMethod = createMethod;
    }

    public String getCreateUser() {
      return createUser;
   }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
      return createDate;
   }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
      return modifyDate;
   }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUser() {
      return modifyUser;
   }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String INSPECT_TASK = "INSPECT_TASK";

    public static final String INSPECT_TYPE = "INSPECT_TYPE";

    public static final String INSPECT_RESULT = "INSPECT_RESULT";

    public static final String WORK_SHOP = "WORK_SHOP";

    public static final String PRODUCT_LINE = "PRODUCT_LINE";

    public static final String OPERATION = "OPERATION";

    public static final String ITEM = "ITEM";

    public static final String SHOP_ORDER = "SHOP_ORDER";

    public static final String SN = "SN";

    public static final String STATE = "STATE";

    public static final String QUALITY_PLAN_BO = "QUALITY_PLAN_BO";

    public static final String CREATE_METHOD = "CREATE_METHOD";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    public static final String MODIFY_USER = "MODIFY_USER";

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }


    public void setObjectSetBasicAttribute( String userId,Date date ){
        this.createUser=userId;
        this.createDate=date;
        this.modifyUser=userId;
        this.modifyDate=date;
   }

    @Override
    public String toString() {
        return "InspectTask{" +
            ", bo = " + bo +
            ", site = " + site +
            ", inspectTask = " + inspectTask +
            ", inspectType = " + inspectType +
            ", inspectResult = " + inspectResult +
            ", workShop = " + workShop +
            ", productLine = " + productLine +
            ", operation = " + operation +
            ", item = " + item +
            ", shopOrder = " + shopOrder +
            ", sn = " + sn +
            ", state = " + state +
            ", qualityPlanBo = " + qualityPlanBo +
            ", createMethod = " + createMethod +
            ", createUser = " + createUser +
            ", createDate = " + createDate +
            ", modifyDate = " + modifyDate +
            ", modifyUser = " + modifyUser +
        "}";
    }
}