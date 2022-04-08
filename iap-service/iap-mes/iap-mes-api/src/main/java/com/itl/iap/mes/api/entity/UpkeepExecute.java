package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_upkeep_execute")
public class UpkeepExecute {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 保养计划名称
     */
    @TableField("upkeepPlanName")
    private String upkeepPlanName;

    /**
     * 保养计划id
     */
    @TableField("upkeepPlanId")
    private String upkeepPlanId;

    /**
     * 设备编号
     */
    @TableField("code")
    private String code;

    /**
     * 设备名称
     */
    @TableField("name")
    private String name;

    /**
     * 状态  0 保存  1完成
     */
    @TableField("state")
    private Integer state;


    /**
     * 设备类型
     */
    @TableField("type")
    private String type;

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型名称")
    @TableField("deviceTypeName")
    private String deviceTypeName;


    /**
     * 产线
     */
    @TableField("productionLine")
    private String productionLine;

    /**
     * 数据收集组id
     */
    @TableField("dataCollectionId")
    private String dataCollectionId;

    /**
     * 数据收集组id
     */
    @TableField("dataCollectionName")
    private String dataCollectionName;

    /**
     * 保养人id
     */
    @TableField("upkeepUserId")
    private String upkeepUserId;

    /**
     * 保养人姓名
     */
    @TableField("upkeepUserName")
    private String upkeepUserName;

    /**
     * 执行时间
     */
    @TableField("executeTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date executeTime;

    @TableField("planExecuteTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("计划保养时间")
    private Date planExecuteTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @TableField(exist = false)
    private DataCollection dataCollection;

    @TableField(exist = false)
    private List<UpkeepExecuteItem> upkeepExecuteItemList;

}
