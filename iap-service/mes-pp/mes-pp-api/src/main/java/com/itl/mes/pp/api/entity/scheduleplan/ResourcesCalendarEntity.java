package com.itl.mes.pp.api.entity.scheduleplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/12/1 9:33
 */

/**
 * 资源日历表：用于排程计划的资源日历
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_resources_calendar")
public class ResourcesCalendarEntity {

    @TableId(value = "ID",type = IdType.UUID)
    private String id;

    @TableField( "SITE")
    private String site;

    @TableField( "WORK_SHOP_BO")
    private String workShopBo = "0";

    @TableField( "PRODUCT_LINE_BO")
    private String productLineBo="0";

    @TableField( "DEVICE_BO")
    private String deviceBo="0";

    @TableField( "USER_BO")
    private String userBo="0";

    @TableField( "RESOURCES_TYPE")
    private String resourcesType;

    @TableField( "DATE")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

    @TableField( "IS_WORK_OVERTIME")
    private String isWorkOvertime;

    @TableField( "CREATE_USER")
    private String createUser;

    @TableField( "CREATE_DATE")
    private Date createDate;

    @TableField( "MODIFY_USER")
    private String modifyUser;

    @TableField( "MODIFY_DATE")
    private Date modifyDate;

    @TableField( "CLASS_FREQUENCY_ID")
    private String classFrequencyId;

    @TableField( "YEAR")
    private Integer year;

    @TableField( "MONTH")
    private Integer month;

    @TableField( "DAY")
    private Integer day;

    @TableField( "STATE")
    private String  state="0";

    @TableField( "START_DATE_STR")
    private String startDateStr;

    @TableField( "END_DATE_STR")
    private String endDateStr;

    @TableField( "WORK_HOUR")
    private Double workHour;

    @TableField( "SORT")
    private Integer sort;
}
