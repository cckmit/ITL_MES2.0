package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_data_collection_adjoin")
public class DataCollectionAdjoin {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /*
    序号
     */
    @TableField("serialNum")
    private Integer serialNum;

    /*
    物料编码
     */
    @TableField("materialCode")
    private String materialCode;

    /*
    物料名称
     */
    @TableField("materialName")
    private String materialName;

    /*
    设备编码
     */
    @TableField("deviceCode")
    private String deviceCode;

    /*
    设备名称
     */
    @TableField("deviceName")
    private String deviceName;

    /*
    设备类型编码
     */
    @TableField("deviceTypeCode")
    private String deviceTypeCode;

    /*
    设备类型名称
     */
    @TableField("deviceTypeName")
    private String deviceTypeName;

    /*
    工序编码
     */
    @TableField("processCode")
    private String processCode;

    /*
    工序名称
     */
    @TableField("processName")
    private String processName;

    /*
    数据收集主表id
     */
    @TableField("dataCollectionId")
    private String dataCollectionId;


}
