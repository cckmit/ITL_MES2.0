package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 安灯
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_andon")
@ApiModel(value = "andon",description = "安灯表")
public class Andon implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("ANDON:SITE,RESOURCE_TYPE,ANDON【PK】")
	@Length( max = 100 )
	@TableId(value = "BO", type = IdType.INPUT)
	private String bo;

	@ApiModelProperty(value="安灯编号")
	@Length( max = 64 )
	@TableField("ANDON")
	private String andon;

	@ApiModelProperty(value="名称")
	@Length( max = 64 )
	@TableField("ANDON_NAME")
	private String andonName;

	@ApiModelProperty(value="描述")
	@Length( max = 256 )
	@TableField("ANDON_DESC")
	private String andonDesc;

	@ApiModelProperty(value="安灯灯箱")
	@Length( max = 100 )
	@TableField("ANDON_BOX_BO")
	private String andonBoxBo;

	@ApiModelProperty(value="安灯类型")
	@Length( max = 100 )
	@TableField("ANDON_TYPE_BO")
	private String andonTypeBo;

	@ApiModelProperty(value="安灯推送设置")
	@Length( max = 100 )
	@TableField("ANDON_PUSH_BO")
	private String andonPushBo;

	@ApiModelProperty(value="工厂")
	@Length( max = 100 )
	@TableField("SITE")
	private String site;

	@ApiModelProperty(value="资源类型")
	@Length( max = 1 )
	@TableField("RESOURCE_TYPE")
	private String resourceType;

	@ApiModelProperty(value="车间")
	@Length( max = 100 )
	@TableField("WORK_SHOP_BO")
	private String workShopBo;

	@ApiModelProperty(value="产线")
	@Length( max = 100 )
	@TableField("PRODUCT_LINE_BO")
	private String productLineBo;

	@ApiModelProperty(value="工位")
	@Length( max = 100 )
	@TableField("STATION_BO")
	private String stationBo;

	@ApiModelProperty(value="设备")
	@Length( max = 100 )
	@TableField("DEVICE_BO")
	private String deviceBo;

	@ApiModelProperty(value="物料")
	@Length( max = 100 )
	@TableField("ITEM_BO")
	private String itemBo;

	@ApiModelProperty(value="状态")
	@Length( max = 1 )
	@TableField("STATE")
	private String state;

	@ApiModelProperty(value="建档日期")
	@TableField("CREATE_DATE")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss",
			timezone = "GMT+8"
	)
	private Date createDate;

	@ApiModelProperty(value="建档人")
	@TableField("CREATE_USER")
	private String createUser;

	@ApiModelProperty(value="修改日期")
	@TableField("MODIFY_DATE")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss",
			timezone = "GMT+8"
	)
	private Date modifyDate;

	@ApiModelProperty(value="修改人")
	@TableField("MODIFY_USER")
	private String modifyUser;


}
