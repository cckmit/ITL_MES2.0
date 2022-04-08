package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.andon.api.constant.CustomCommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * 安灯灯箱
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_box")
@ApiModel(value = "Box", description = "安灯灯箱表")
@Accessors(chain = true)
public class Box implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "BOX:SITE,BOX【PK】")
    @Length(max = 100)
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value = "灯箱编号【UK】")
    @Length(max = 64)
    @TableField("BOX")
    private String box;

    @ApiModelProperty(value = "名称")
    @Length(max = 64)
    @TableField("BOX_NAME")
    private String boxName;

    @ApiModelProperty(value = "描述")
    @Length(max = 256)
    @TableField("BOX_DESC")
    private String boxDesc;

    @ApiModelProperty(value = "工厂")
    @Length(max = 100)
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value = "资源类型")
    @Length(max = 5)
    @TableField("RESOURCE_TYPE")
    private String resourceType;

    @ApiModelProperty(value = "车间")
    @Length(max = 100)
    @TableField("WORK_SHOP_BO")
    private String workShopBo;

    @ApiModelProperty(value = "产线")
    @Length(max = 100)
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;

    @ApiModelProperty(value = "工位")
    @Length(max = 100)
    @TableField("STATION_BO")
    private String stationBo;

    @ApiModelProperty(value = "设备")
    @Length(max = 100)
    @TableField("DEVICE_BO")
    private String deviceBo;

    @ApiModelProperty(value = "状态")
    @Length(max = 1)
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value = "建档日期")
    @TableField("CREATE_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "建档人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "修改日期")
    @TableField("MODIFY_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;

    public void setObjectSetBasicAttribute( String userId,Date date ){
        this.createUser=userId;
        this.createDate=date;
        this.modifyUser=userId;
        this.modifyDate=date;
    }

    public static final String BO = "BO";

    public static final String BOX = "BOX";

    public static final String BOX_NAME = "BOX_NAME";

    public static final String BOX_DESC = "BOX_DESC";

    public static final String SITE = "SITE";

    public static final String RESOURCE_TYPE = "RESOURCE_TYPE";

    public static final String WORK_SHOP_BO = "WORK_SHOP_BO";

    public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

    public static final String STATION_BO = "STATION_BO";

    public static final String DEVICE_BO = "DEVICE_BO";

    public static final String STATE = "STATE";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    public static final String MODIFY_USER = "MODIFY_USER";
}
