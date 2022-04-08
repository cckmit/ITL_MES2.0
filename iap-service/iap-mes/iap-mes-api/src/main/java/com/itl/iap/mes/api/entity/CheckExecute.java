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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_check_execute")
public class CheckExecute {
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
     * 数据收集组
     */
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
     * 状态  0 保存  1完成
     */
    @TableField("state")
    private Integer state;

    /**
     * 执行时间
     */

    @TableField("executeTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date executeTime;

    @TableField("planExecuteTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planExecuteTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @TableField(exist = false)
    private List<CheckExecuteItem> checkExecuteItemList;

    @TableField("siteId")
    private String siteId;
}
