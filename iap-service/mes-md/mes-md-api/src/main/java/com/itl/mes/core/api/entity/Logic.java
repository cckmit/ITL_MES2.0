package com.itl.mes.core.api.entity;

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
 * SQL语句表
 * </p>
 *
 * @author space
 * @since 2019-11-21
 */
@TableName("z_logic")
@ApiModel(value="Logic",description="SQL语句表")
public class Logic extends Model<Logic> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="编号主键")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="逻辑编号")
    @Length( max = 64 )
    @TableField("LOGIC_NO")
    private String logicNo;

    @ApiModelProperty(value="版本")
    @Length( max = 10 )
    @TableField("VERSION")
    private String version;

    @ApiModelProperty(value="描述")
    @Length( max = 256 )
    @TableField("LOGIC_DESC")
    private String logicDesc;

    @ApiModelProperty(value="Y/N")
    @Length( max = 2 )
    @TableField("IS_CURRENT_VERSION")
    private String isCurrentVersion;

    @ApiModelProperty(value="SQL内容")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value="建档人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
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

    public String getLogicNo() {
      return logicNo;
   }

    public void setLogicNo(String logicNo) {
        this.logicNo = logicNo;
    }

    public String getVersion() {
      return version;
   }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLogicDesc() {
      return logicDesc;
   }

    public void setLogicDesc(String logicDesc) {
        this.logicDesc = logicDesc;
    }

    public String getIsCurrentVersion() {
      return isCurrentVersion;
   }

    public void setIsCurrentVersion(String isCurrentVersion) {
        this.isCurrentVersion = isCurrentVersion;
    }

    public String getContent() {
      return content;
   }

    public void setContent(String content) {
        this.content = content;
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

    public static final String LOGIC_NO = "LOGIC_NO";

    public static final String VERSION = "VERSION";

    public static final String LOGIC_DESC = "LOGIC_DESC";

    public static final String IS_CURRENT_VERSION = "IS_CURRENT_VERSION";

    public static final String CONTENT = "CONTENT";

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
        return "Logic{" +
            ", bo = " + bo +
            ", logicNo = " + logicNo +
            ", version = " + version +
            ", logicDesc = " + logicDesc +
            ", isCurrentVersion = " + isCurrentVersion +
            ", content = " + content +
            ", createUser = " + createUser +
            ", createDate = " + createDate +
            ", modifyDate = " + modifyDate +
            ", modifyUser = " + modifyUser +
        "}";
    }
}