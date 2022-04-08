package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sky,
 * @date 2019/6/17
 * @time 13:36
 */
@Data
@ApiModel(value = "DeviceVo", description = "保存设备数据使用")
public class DeviceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="设备编号【UK】")
    private String device;

    @ApiModelProperty(value="设备名称")
    private String deviceName;

    @ApiModelProperty(value="设备描述")
    private String deviceDesc;

    @ApiModelProperty(value="设备型号")
    private String deviceModel;

    @ApiModelProperty(value="状态")
    private String state;

//    @ApiModelProperty(value="是否为加工设备")
//    private String isProcessDevice;

    @ApiModelProperty(value="产线")
    private String productLine;

    @ApiModelProperty(value="工位")
    private String station;

    @ApiModelProperty(value="安装地点")
    private String location;

    @ApiModelProperty(value="资产编号")
    private String assetsCode;

    @ApiModelProperty(value="生产厂家")
    private String manufacturer;

    @ApiModelProperty(value="投产日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private Date validStartDate;

//    @ApiModelProperty(value="结束日期")
//    @JsonFormat(
//            pattern = "yyyy-MM-dd HH:mm:ss",
//            timezone = "GMT+8"
//    )
//    private Date validEndDate;

    @ApiModelProperty(value="备注")
    private String memo;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyDate;

    @ApiModelProperty( value="可分配设备类型" )
    private List<DeviceTypeSimplifyVo> availableDeviceTypeList;

    @ApiModelProperty( value="已分配设备类型" )
    private List<DeviceTypeSimplifyVo> assignedDeviceTypeList;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;

//    @ApiModelProperty( value = "螺杆组合" )
//    private String screwCombination;

    @ApiModelProperty( value = "设备类型" )
    private String deviceType;

    @ApiModelProperty( value = "生产日期" )
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private Date productionDate;

    @ApiModelProperty( value = "工序" )
    private String operationBo;

    @ApiModelProperty( value = "进厂日期" )
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private Date joinFactoryDate;

    @ApiModelProperty( value = "负责人" )
    private String responsiblePerson;

    @ApiModelProperty( value = "设备图片" )
    private String deviceImage;
    @ApiModelProperty( value = "车间" )
    private String workShop;
    @ApiModelProperty( value = "车间名称" )
    private String workShopName;

    @ApiModelProperty(value="产品（物料）BO")
    private String itemBo;

    @ApiModelProperty(value="产品（物料）名称")
    private String itemName;

}
