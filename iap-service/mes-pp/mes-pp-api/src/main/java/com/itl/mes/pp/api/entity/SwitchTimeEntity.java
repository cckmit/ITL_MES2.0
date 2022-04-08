package com.itl.mes.pp.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 生产切换时间
 * @author yaoxiang
 * @date 2020/12/17
 * @since JDK1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_product_switch_time")
@ApiModel(value = "p_product_switch_time", description = "生产切换时间表")
public class SwitchTimeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "BO", type = IdType.UUID)
    @ApiModelProperty("ID")
    @Excel(name = "BO")
    private String bo;

    @TableField("PROCESS_CHARACTERISTICS")
    @ApiModelProperty("工艺特性")
    @Excel(name = "工艺特性")
    private String processCharacteristics;

    @TableField("TARGET_PROCESS_CHARACTERISTICS")
    @ApiModelProperty("目标工艺特性")
    @Excel(name = "目标工艺特性")
    private String targetProcessCharacteristics;

    @TableField("SWITCH_TYPE")
    @ApiModelProperty("切换类型")
    @Excel(name = "切换类型")
    private String switchType;

    @TableField("SWITCH_TIME")
    @ApiModelProperty("切换时间")
    @Excel(name = "切换时间")
    private Float switchTime;

    @TableField("SITE")
    @ApiModelProperty("工厂")
    @Excel(name = "工厂")
    private String site;

    @TableField("CREATE_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    @ApiModelProperty("创建时间")
    @Excel(name = "创建时间", width = 30, format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createDate;

    @TableField("CREATE_USER")
    @ApiModelProperty("创建人")
    @Excel(name = "创建人")
    private String createUser;

    @TableField("MODIFY_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    @ApiModelProperty("修改时间")
    @Excel(name = "修改时间", width = 30, format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date modifyDate;

    @TableField("MODIFY_USER")
    @ApiModelProperty("修改人")
    @Excel(name = "修改人")
    private String modifyUser;

    public void setObjectSetBasicAttribute( String userId,Date date ){
        this.createUser=userId;
        this.createDate=date;
        this.modifyUser=userId;
        this.modifyDate=date;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String PROCESS_CHARACTERISTICS = "PROCESS_CHARACTERISTICS";

    public static final String TARGET_PROCESS_CHARACTERISTICS = "TARGET_PROCESS_CHARACTERISTICS";

    public static final String SWITCH_TYPE = "SWITCH_TYPE";

    public static final String SWITCH_TIME = "SWITCH_TIME";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    public static final String MODIFY_USER = "MODIFY_USER";
}
