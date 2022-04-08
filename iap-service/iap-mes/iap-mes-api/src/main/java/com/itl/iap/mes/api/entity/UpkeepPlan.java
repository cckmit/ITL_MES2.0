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


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_upkeep_plan")
public class UpkeepPlan {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 保养计划名称
     */
    @TableField("upkeepPlanName")
    private String upkeepPlanName;
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
     * 设备类型
     */
    @TableField("type")
    private String type;
    /**
     * 数据收集组id
     */
    @TableField("dataCollectionId")
    private String dataCollectionId;

    /**
     * 保养人id
     */
    @TableField("upkeepUserId")
    private String upkeepUserId;

    /**
     * 保养人姓名(提醒人员)
     */
    @TableField("upkeepUserName")
    private String upkeepUserName;

    /**
     * 开始时间（保养时间）
     */
    @TableField("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;


    /**
     * 结束时间
     */
    @TableField("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;


    /**
     * 状态
     */
    @TableField("state")
    private Integer state;

    //间隔  每多长时间执行
    @TableField("cycle")
    private Integer cycle;

    @ApiModelProperty("0 季度 1 周 2 日")
    @TableField("ytd")
    private Integer ytd;


    @TableField(exist = false)
    private String stateName;


    /**
     * 描述
     */
    @TableField("remark")
    private String remark;

    @TableField("dataCollectionName")
    private String dataCollectionName;


    /**
     * 定时任务id集合
     */
    @TableField("jobIds")
    private String jobIds;


    /**
     * 执行状态   0 未执行   1执行中
     */
    @TableField("runState")
    private Integer runState;

    /**
     *创建时间
     */
    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 设备类型名称
     */
    @TableField("deviceTypeName")
    private String deviceTypeName;

    /**
     * 创建人姓名
     */
    @TableField("createName")
    private String createName;

    /**
     * 创建人账号
     */
    @TableField("createAccount")
    private String createAccount;

    @ApiModelProperty("保养单编号")
    @TableField("repairId")
    private String repairId;



}