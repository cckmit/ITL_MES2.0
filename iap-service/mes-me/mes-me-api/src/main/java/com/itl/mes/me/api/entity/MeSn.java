package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 条码表
 *
 * @author cuichonghe
 * @date 2021-01-25 14:43:26
 */
@Data
@TableName("me_sn")
@ApiModel(value = "me_sn", description = "comments")
@Accessors(chain = true)
public class MeSn implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * SN
	 */
	@TableId
	@ApiModelProperty(value = "SN")
	@TableField("SN")
	private String sn;
	/**
	 * 工厂
	 */
	@ApiModelProperty(value = "工厂")
	@TableField("SITE")
	private String site;
	/**
	 * 条码类型
	 */
	@ApiModelProperty(value = "条码类型")
	@TableField("SN_TYPE_BO")
	private String snTypeBo;
	/**
	 * 建档日期
	 */
	@ApiModelProperty(value = "建档日期")
	@TableField("CREATE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date createDate;
	/**
	 * 建档人
	 */
	@ApiModelProperty(value = "建档人")
	@TableField("CREATE_USER")
	private String createUser;
	/**
	 * 修改日期
	 */
	@ApiModelProperty(value = "修改日期")
	@TableField("MODIFY_DATE")
	private Date modifyDate;
	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	@TableField("MODIFY_USER")
	private String modifyUser;

	public void setObjectSetBasicAttribute( String userId,Date date ){
		this.createUser=userId;
		this.createDate=date;
		this.modifyUser=userId;
		this.modifyDate=date;
	}
}
