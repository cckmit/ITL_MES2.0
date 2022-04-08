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

/**
 * <p>
 * 
 * </p>
 *
 * @author space
 * @since 2019-08-29
 */
@TableName("m_attached")
@ApiModel(value="Attached",description="")
public class Attached extends Model<Attached> {

    private static final long serialVersionUID = 1L;


    @Excel( name="表BO", orderNum="7")
    @ApiModelProperty(value="AE:SITE,ATTACH_FROM_BO,SEQ,ATTACH_KEY[PK]")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @Excel( name="工厂", orderNum="6")
    @ApiModelProperty(value="工厂")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @Excel( name="附加对象的BO", orderNum="0")
    @ApiModelProperty(value="附加对象的BO")
    @Length( max = 100 )
    @TableField("ATTACHED_FROM_BO")
    private String attachedFromBo;

    @Excel( name="附加对象的序列", orderNum="1")
    @ApiModelProperty(value="附加对象的序列")
    @TableField("SEQ")
    private Integer seq;

    @Excel( name="附加内容BO", orderNum="2")
    @ApiModelProperty(value="附加内容BO")
    @Length( max = 100 )
    @TableField("CONTEXT_BO")
    private String contextBo;

    @Excel( name="ATTACH_FROM_BO_SEQ附加总个数", orderNum="3")
    @ApiModelProperty(value="ATTACH_FROM_BO_SEQ附加总个数")
    @TableField("COUNT_TOTAL")
    private Integer countTotal;

    @Excel( name="ATTACH_FROM_BO_SEQ的个数从1开始", orderNum="4")
    @ApiModelProperty(value="ATTACH_FROM_BO_SEQ的个数从1开始")
    @TableField("ATTACHED_KEY")
    private Integer attachedKey;

    @Excel( name="附加类型", orderNum="5")
    @ApiModelProperty(value="附加类型(D:数据收集，Q:质量控制计划)")
    @Length( max = 10 )
    @TableField("ATTACHED_TYPE")
    private String attachedType;


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

    public String getAttachedFromBo() {
      return attachedFromBo;
   }

    public void setAttachedFromBo(String attachedFromBo) {
        this.attachedFromBo = attachedFromBo;
    }

    public Integer getSeq() {
      return seq;
   }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getContextBo() {
      return contextBo;
   }

    public void setContextBo(String contextBo) {
        this.contextBo = contextBo;
    }

    public Integer getCountTotal() {
      return countTotal;
   }

    public void setCountTotal(Integer countTotal) {
        this.countTotal = countTotal;
    }

    public Integer getAttachedKey() {
      return attachedKey;
   }

    public void setAttachedKey(Integer attachedKey) {
        this.attachedKey = attachedKey;
    }

    public String getAttachedType() {
      return attachedType;
   }

    public void setAttachedType(String attachedType) {
        this.attachedType = attachedType;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String ATTACHED_FROM_BO = "ATTACHED_FROM_BO";

    public static final String SEQ = "SEQ";

    public static final String CONTEXT_BO = "CONTEXT_BO";

    public static final String COUNT_TOTAL = "COUNT_TOTAL";

    public static final String ATTACHED_KEY = "ATTACHED_KEY";

    public static final String ATTACHED_TYPE = "ATTACHED_TYPE";

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }



    @Override
    public String toString() {
        return "Attached{" +
            ", bo = " + bo +
            ", site = " + site +
            ", attachedFromBo = " + attachedFromBo +
            ", seq = " + seq +
            ", contextBo = " + contextBo +
            ", countTotal = " + countTotal +
            ", attachedKey = " + attachedKey +
            ", attachedType = " + attachedType +
        "}";
    }
}