package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author space
 * @since 2019-08-29
 */
@TableName("m_quality_plan")
@ApiModel(value="QualityPlan",description="")
public class QualityPlan extends Model<QualityPlan> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="QP:SITE,QUALITY_PLAN,VERSION[PK]")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    @Excel( name="表BO", orderNum="9" )
    private String bo;

    @ApiModelProperty(value="工厂")
    @Length( max = 32 )
    @TableField("SITE")
    @Excel( name="工厂", orderNum="8" )
    private String site;

    @ApiModelProperty(value="质量控制计划")
    @Length( max = 64 )
    @TableField("QUALITY_PLAN")
    @Excel( name="质量控制计划", orderNum="0" )
    private String qualityPlan;

    @ApiModelProperty(value="质量控制计划描述")
    @Length( max = 200 )
    @TableField("QUALITY_PLAN_DESC")
    @Excel( name="质量控制计划描述", orderNum="1" )
    private String qualityPlanDesc;

    @ApiModelProperty(value="是否当前版本(Y：是，N：不是)")
    @Length( max = 1 )
    @TableField("IS_CURRENT_VERSION")
    @Excel( name="是否当前版本", orderNum="3" )
    private String isCurrentVersion;

    @ApiModelProperty(value="创建人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    @Excel( name="创建人", orderNum="4" )
    private String createUser;

    @ApiModelProperty(value="创建时间")
    @TableField("CREATE_DATE")
    @Excel( name="创建时间", orderNum="5",exportFormat = "yyyy-MM-dd" ,importFormat = "yyyy-MM-dd")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("MODIFY_USER")
    @Excel( name="修改人", orderNum="6" )
    private String modifyUser;

    @ApiModelProperty(value="修改时间")
    @TableField("MODIFY_DATE")
    @Excel( name="修改时间", orderNum="7" ,exportFormat = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    public String getQualityPlanDesc() {
        return qualityPlanDesc;
    }

    public void setQualityPlanDesc(String qualityPlanDesc) {
        this.qualityPlanDesc = qualityPlanDesc;
    }

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

    public String getQualityPlan() {
      return qualityPlan;
   }

    public void setQualityPlan(String qualityPlan) {
        this.qualityPlan = qualityPlan;
    }

    public String getIsCurrentVersion() {
      return isCurrentVersion;
   }

    public void setIsCurrentVersion(String isCurrentVersion) {
        this.isCurrentVersion = isCurrentVersion;
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

    public String getModifyUser() {
      return modifyUser;
   }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyDate() {
      return modifyDate;
   }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String QUALITY_PLAN = "QUALITY_PLAN";

    public static final String QUALITY_PLAN_DESC = "QUALITY_PLAN_DESC";

    public static final String VERSION = "VERSION";

    public static final String IS_CURRENT_VERSION = "IS_CURRENT_VERSION";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String MODIFY_USER = "MODIFY_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

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
        return "QualityPlan{" +
                "bo='" + bo + '\'' +
                ", site='" + site + '\'' +
                ", qualityPlan='" + qualityPlan + '\'' +
                ", qualityPlanDesc='" + qualityPlanDesc + '\'' +
                ", isCurrentVersion='" + isCurrentVersion + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createDate=" + createDate +
                ", modifyUser='" + modifyUser + '\'' +
                ", modifyDate=" + modifyDate +
                '}';
    }

    public QualityPlan(){};

    public QualityPlan(String bo, String site, String qualityPlan, String qualityPlanDesc, String isCurrentVersion, String createUser, Date createDate, String modifyUser, Date modifyDate) {
        this.bo = bo;
        this.site = site;
        this.qualityPlan = qualityPlan;
        this.qualityPlanDesc = qualityPlanDesc;
        this.isCurrentVersion = isCurrentVersion;
        this.createUser = createUser;
        this.createDate = createDate;
        this.modifyUser = modifyUser;
        this.modifyDate = modifyDate;
    }
}