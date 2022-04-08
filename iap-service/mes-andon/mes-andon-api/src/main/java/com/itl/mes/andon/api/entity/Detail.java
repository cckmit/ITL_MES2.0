package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("andon_detail")
@ApiModel(value = "detail", description = "安灯明细")
@Accessors(chain = true)
public class Detail extends Model<Detail> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value="id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @TableField("andon_desc")
    @Length(max = 255)
    @ApiModelProperty(value = "安灯异常明细")
    private String andonDesc;

    @TableField("andon_desc_bo")
    @Length(max = 255)
    @ApiModelProperty(value = "安灯异常明细")
    private String andonDescBo;

    @TableField("andon_fault")
    @Length(max = 32)
    @ApiModelProperty(value = "安灯故障")
    private String andonFault;

    @TableField("andon_type_bo")
    @Length(max = 100)
    @ApiModelProperty(value = "安灯类型编号")
    private String andonTypeBO;

    @TableField("andon_name")
    @Length(max = 64)
    @ApiModelProperty(value = "安灯类型名称")
    private String andonName;

    @TableField("remark")
    @Length(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField("state")
    @Length(max = 1)
    @ApiModelProperty(value = "状态")
    private String state;

    /** 建档日期 */
    @TableField("CREATE_DATE")
    @ApiModelProperty(value = "建档日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;
    /** 建档人 */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "建档人")
    private String createUser;
    /** 修改日期 */
    @TableField("MODIFY_DATE")
    @ApiModelProperty(value = "修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;
    /** 修改人 */
    @TableField("MODIFY_USER")
    @ApiModelProperty(value = "修改人")
    private String modifyUser;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
