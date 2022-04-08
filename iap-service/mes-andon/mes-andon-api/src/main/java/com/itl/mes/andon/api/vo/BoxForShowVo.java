package com.itl.mes.andon.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.andon.api.constant.CustomCommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yaoxiang
 * @date 2020/12/17
 * @since JDK1.8
 */
@Data
@ApiModel(value = "BoxForShow", description = "安灯灯箱展示")
@Accessors(chain = true)
public class BoxForShowVo{
    @ApiModelProperty(value = "BOX:SITE,BOX【PK】")
    private String bo;

    @ApiModelProperty(value = "灯箱编号【UK】")
    private String box;

    @ApiModelProperty(value = "名称")
    private String boxName;

    @ApiModelProperty(value = "描述")
    private String boxDesc;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "资源类型")
    private String resourceType;

    @ApiModelProperty(value = "车间")
    private String workShop;

    @ApiModelProperty(value = "产线")
    private String productLine;

    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "设备")
    private String device;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "建档日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "建档人")
    private String createUser;

    @ApiModelProperty(value = "修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "修改人")
    private String modifyUser;
}
