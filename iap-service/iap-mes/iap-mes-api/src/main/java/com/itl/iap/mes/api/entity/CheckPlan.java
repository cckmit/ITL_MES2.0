package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_check_plan")
public class CheckPlan {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 点检计划名称
     */
    @TableField("checkPlanName")
    private String checkPlanName;
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
     * 设备类型名称
     */
    @TableField("device_type_name")
    private String deviceTypeName;

    /**
     * 数据收集组id
     */
    @TableField("dataCollectionId")
    private String dataCollectionId;

    @TableField("dataCollectionName")
    private String dataCollectionName;

    /**
     * 点检人id
     */
    @TableField("checkUserId")
    private String checkUserId;

    /**
     * 点检人姓名
     */
    @TableField("checkUserName")
    private String checkUserName;

    /**
     * 开始时间
     */
    @TableField("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;


    /**
     * 结束时间
     */
   /* @TableField("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;*/


    /**
     * 状态
     */
    @TableField("state")
    private Integer state;

    //间隔  每多长时间执行
   /* @TableField("cycle")
    private Integer cycle;*/

    //月 天 时  的常量  0月1天2时
    @TableField("ytd")
    private Integer ytd;


    @TableField(exist = false)
    private String stateName;


    /**
     * 描述
     */
    @TableField("remark")
    private String remark;


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
     * 工厂id
     */
    @TableField("siteId")
    private String siteId;

    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField("createPerson")
    private String createPerson;

    @TableField(exist = false)
    private String device;
}