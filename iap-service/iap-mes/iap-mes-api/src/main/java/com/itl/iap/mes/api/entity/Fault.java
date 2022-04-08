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
@TableName("m_repair_fault")
public class Fault {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 工厂编码
     */
    @TableField("planCode")
    private String planCode;
    /**
     * 故障编码
     */
    @TableField("faultCode")
    private String faultCode;
    /**
     * 描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 设备类型
     */
    @TableField("type")
    private String type;

    /**
     * 状态
     */
    @TableField("state")
    private Integer state;


    /**
     * 维修方法
     */
    @TableField("repairMethod")
    private String repairMethod;

    /**
     * 工厂id
     */
    @TableField("siteId")
    private String siteId;

    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}