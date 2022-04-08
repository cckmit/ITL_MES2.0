package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_corrective_maintenance")
public class CorrectiveMaintenance {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 设备类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 设备编号
     */
    @TableField("code")
    private String code;

    /**
     * 故障编码
     */
    @TableField("faultCode")
    private String faultCode;

    /**
     * 维修状态
     */
    @TableField("state")
    private Integer state;

    @TableField(exist = false)
    private String stateName;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 产线
     */
    @TableField("productionLine")
    private String productionLine;


    /**
     * 工位
     */
    @TableField("station")
    private String station;


    /**
     * 发生时间
     */
    @TableField("happenTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date happenTime;


    /**
     * 计划维修人员id
     */
    @TableField("planRepairUserId")
    private String planRepairUserId;

    /**
     * 计划维修人员姓名
     */
    @TableField("planRepairUserName")
    private String planRepairUserName;


    /**
     * 维修人员id
     */
    @TableField("repairUserId")
    private String repairUserId;

    /**
     * 维修人员姓名
     */
    @TableField("repairUserName")
    private String repairUserName;

    /**
     * 维修开始时间
     */
    @TableField("repairStartTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date repairStartTime;

    /**
     * 维修结束时间
     */
    @TableField("repairEndTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date repairEndTime;


    @TableField(exist = false)
    private List<MesFiles> mesFilesList;

    /**
     * 维修人员姓名
     */
    @TableField("siteId")
    private String siteId;



    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    @TableField(exist = false)
    private String endTime;


    @TableField(exist = false)
    private String startTime;
    /**
     * 维修过程描述
     */
    @TableField("repairProcessDesc")
    private String repairProcessDesc;
}
