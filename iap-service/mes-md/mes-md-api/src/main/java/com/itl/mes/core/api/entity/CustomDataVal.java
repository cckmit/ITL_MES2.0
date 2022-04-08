package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 * 自定义数据的值
 * </p>
 *
 * @author space
 * @since 2019-05-29
 */
@TableName("m_custom_data_val")
@ApiModel(value="CustomDataVal",description="自定义数据的值")
public class CustomDataVal extends Model<CustomDataVal> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="特定对象的BO")
    @Length( max = 200 )
    @TableField("BO")
    @Excel( name="特定对象的BO", orderNum="0" )
    private String bo;

    @ApiModelProperty(value="自定义数据BO")
    @Length( max = 200 )
    @TableField("CUSTOM_DATA_BO")
    @Excel( name="自定义数据BO", orderNum="1" )
    private String customDataBo;

    @ApiModelProperty(value="对应CD_FIELD")
    @Length( max = 200 )
    @TableField("ATTRIBUTE")
    @Excel( name="对应CD_FIELD", orderNum="2" )
    private String attribute;

    @ApiModelProperty(value="值")
    @Length( max = 4000 )
    @TableField("VALS")
    @Excel( name="值", orderNum="3" )
    private String vals;


    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getCustomDataBo() {
        return customDataBo;
    }

    public void setCustomDataBo(String customDataBo) {
        this.customDataBo = customDataBo;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getVals() {
        return vals;
    }

    public void setVals(String vals) {
        this.vals = vals;
    }

    public static final String BO = "BO";

    public static final String CUSTOM_DATA_BO = "CUSTOM_DATA_BO";

    public static final String ATTRIBUTE = "ATTRIBUTE";

    public static final String VALS = "VALS";

    @Override
    protected Serializable pkVal() {
        return this.bo+this.attribute;
    }


    @Override
    public String toString() {
        return "CustomDataVal{" +
                ", bo = " + bo +
                ", customDataBo = " + customDataBo +
                ", attribute = " + attribute +
                ", vals = " + vals +
                "}";
    }
}