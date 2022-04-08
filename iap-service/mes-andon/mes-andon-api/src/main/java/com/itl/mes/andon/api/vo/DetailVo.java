package com.itl.mes.andon.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "DetailVO", description = "保存安灯明细数据")
public class DetailVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "安灯异常明细")
    private String andonDesc;

    @ApiModelProperty(value = "安灯异常明细")
    private String andonDescBo;

    @ApiModelProperty(value = "安灯故障")
    private String andonFault;

    @ApiModelProperty(value = "安灯类型编号")
    private String andonTypeBO;

    @ApiModelProperty(value = "安灯类型名称")
    private String andonName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private String state;

    /** 建档日期 */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    /** 建档人 */
    @ApiModelProperty(value = "建档人")
    private String createUser;

    /** 修改日期 */
    @ApiModelProperty(value = "修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private String modifyUser;
}
