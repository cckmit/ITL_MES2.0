package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "MouldVo",description = "模具VO")
public class MouldVo implements Serializable {

    private static final long serialVersionUID = 1226244479174008255L;


    @ApiModelProperty(value = "主键")
    private String Bo;

    @ApiModelProperty(value = "模具编码(设备编码)")
    private String mould;

    @ApiModelProperty(value = "模具名称")
    private String mouldName;

    @ApiModelProperty(value = "车间BO")
    private String workShopBo;

    @ApiModelProperty(value = "工序BO")
    private String operationBo;

    @ApiModelProperty(value = "产品（物料）BO")
    private String itemBo;

    @ApiModelProperty(value = "领用人员（realName）")
    private String collectUser;

    @ApiModelProperty(value = "归还人员（realName）")
    private String returnUser;

    @ApiModelProperty(value = "领用时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private String collectDate;

    @ApiModelProperty(value = "归还时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private String returnDate;

    @ApiModelProperty(value = "状态（0待归还，1已完成)")
    private String state;
}
