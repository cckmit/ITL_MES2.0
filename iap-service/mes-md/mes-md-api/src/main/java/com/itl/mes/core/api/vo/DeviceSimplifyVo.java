package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/6/19
 * @time 17:17
 */
@Data
@ApiModel(value = "DeviceSimplifyVo", description = "保存设备类型里的设备数据使用")
public class DeviceSimplifyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="设备编号【UK】")
    private String device;

    @ApiModelProperty(value="设备名称")
    private String deviceName;

    @ApiModelProperty(value="设备描述")
    private String deviceDesc;
}
