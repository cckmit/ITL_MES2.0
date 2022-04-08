package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Sfc包装
 *
 * @author cuichonghe
 * @date 2021-01-25 14:43:26
 */
@Data
@TableName("me_sfc_packing")
@ApiModel(value = "me_sfc_packing", description = "comments")
@Accessors(chain = true)
public class MeSfcPacking implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一标识【PK】
	 */
	@TableId(value = "BO", type = IdType.UUID)
	@ApiModelProperty(value = "唯一标识【PK】")
	private String bo;
	/**
	 * 工厂
	 */
	@ApiModelProperty(value = "工厂")
	@TableField("SITE")
	private String site;
	/**
	 * carton号
	 */
	@ApiModelProperty(value = "carton号")
	@TableField("CARTON_NO")
	private String cartonNo;
	/**
	 * SFC过程
	 */
	@ApiModelProperty(value = "SFC过程")
	@TableField("WIP_LOG_BO")
	private String wipLogBo;
	/**
	 * 箱号创建时间
	 */
	@ApiModelProperty(value = "箱号创建时间")
	@TableField("CREATE_TIME")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss.SSS",
			timezone = "GMT+8"
	)
	private Date createTime;
	/**
	 * 结束装箱时间
	 */
	@ApiModelProperty(value = "结束装箱时间")
	@TableField("FINISH_TIME")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss.SSS",
			timezone = "GMT+8"
	)
	private Date finishTime;
	/**
	 * 最大容量
	 */
	@ApiModelProperty(value = "最大容量")
	@TableField("MAX_COUNT")
	private BigDecimal maxCount;
	/**
	 * 当前数据
	 */
	@ApiModelProperty(value = "当前数据")
	@TableField("CURRENT_COUNT")
	private BigDecimal currentCount;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	@TableField("STATE")
	private Integer state;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	@TableField("REMARK")
	private String remark;

}
