package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/6/19
 * @time 17:12
 */
@Data
@ApiModel(value = "DeviceTypeSimplifyVo", description = "保存设备里的设备类型数据使用")
public class DeviceTypeSimplifyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="编号【UK】")
    private String deviceType;

    @ApiModelProperty(value="名称")
    private String deviceTypeName;

    @ApiModelProperty(value="描述")
    private String deviceTypeDesc;
}
