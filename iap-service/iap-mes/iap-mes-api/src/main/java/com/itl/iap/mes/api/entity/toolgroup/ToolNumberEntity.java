package com.itl.iap.mes.api.entity.toolgroup;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/6 14:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_tool_number")
public class ToolNumberEntity {



    @TableId(type = IdType.UUID)
    private String bo;


    @TableField("SITE")
    private String site;

    @TableField("TOOL_NUMBER")
    private String toolNumber;

    @TableField("TOOL_GROUP_BO")
    private String toolGroupBo;

    @TableField(exist = false)
    private String toolGroup;

    @TableField(exist = false)
    private Integer toolGroupQty;

    @TableField("TN_DESC")
    private String tnDesc;


    @TableField("STATE")
    private String state;

    @TableField("QTY")
    private Integer qty;


    @TableField("CALIBRATION_TYPE")
    private String calibrationType;

    @TableField("CALIBRATION_DATE")
    private Date calibrationDate;

    @TableField("CALIBRATION_PERIOD")
    private Integer calibrationPeriod;

    @TableField("CALIBRATION_PERIOD_TYPE")
    private String calibrationPeriodType;


    @TableField("EXP_DATE")
    private String expDate;

    @TableField("LOCATION")
    private String location;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("CREATE_DATE")
    private Date createDate;

    @TableField("MODIFY_USER")
    private String modifyUser;

    @TableField("MODIFY_DATE")
    private Date modifyDate;



}
