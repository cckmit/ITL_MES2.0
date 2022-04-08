package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sky,
 * @date 2019/6/3
 * @time 10:30
 */
@Data
@ApiModel(value = "DataListFullVo", description = "保存数据列表使用")
public class DataListFullVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="列表编号【PK】")
    private String dataList;

    @ApiModelProperty(value="列表名")
    private String listName;

    @ApiModelProperty(value="列表描述")
    private String listDesc;


    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value="数据列表参数集合")
    private List<ListParameterVo> ListParameterVoList;

}
