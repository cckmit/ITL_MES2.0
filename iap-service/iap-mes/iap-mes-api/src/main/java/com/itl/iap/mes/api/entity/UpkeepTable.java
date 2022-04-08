package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("m_repair_upkeep_table")
@ApiModel(value="UpkeepTable",description="保养单表")
public class UpkeepTable extends Model<UpkeepTable> {


    private static final long serialVersionUID = 2413092187270265238L;

    @ApiModelProperty(value="id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 保养单编号
     */
    @TableField("repairId")
    private String repairId;

    /**
     * 保养时间
     */
    @TableField("repairTime")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date repairTime;

    /**
     * 设备编号
     */
    @TableField("deviceCode")
    private String deviceCode;

    /**
     * 设备名称
     */
    @TableField("deviceName")
    private String deviceName;

    /**
     * 保养人账号
     */
    @TableField("repairUserName")
    private String repairUserName;

    /**
     * 保养人名字
     */
    @TableField("repairRealName")
    private String repairRealName;

    /**
     * 设备保养执行id
     */
    @TableField("executeId")
    private String executeId;

    /**
     * 保养单状态【提交 1 已保养 保存 0 保养中】
     */
    @TableField("state")
    private String state;

    /**
     * 设备类型
     */
    @TableField("deviceType")
    private String deviceType;

    @TableField("planExecuteTime")
    @ApiModelProperty("计划执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planExecuteTime;

    @ApiModelProperty("数据收集组名称")
    @TableField("dataCollectionName")
    private String dataCollectionName;

    @ApiModelProperty("计划执行名称")
    @TableField("upkeepPlanName")
    private String upkeepPlanName;

    @ApiModelProperty(value="巡检人员名字")
    @TableField("inspection_userName")
    private String inspectionUserName;

    @ApiModelProperty("设备类型名称")
    @TableField(exist = false)
    private String deviceTypeName;


    @TableField(exist = false)
    private List<DataCollectionItem> dataCollectionItems;

    @TableField(exist = false)
    private List<UpkeepTableItem> upkeepTableItems;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
