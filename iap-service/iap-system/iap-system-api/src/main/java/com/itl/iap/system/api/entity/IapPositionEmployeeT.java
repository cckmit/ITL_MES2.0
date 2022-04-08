package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author mjl 岗位-员工 中间表
 * @since 2020-06-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iap_position_employee_t")
public class IapPositionEmployeeT {

    /**
     * 主键id
     */
    @TableId
    private String id;

    /**
     * 岗位id
     */
    @TableField("position_id")
    private String positionId;

    /**
     * 员工id
     */
    @TableField("employee_id")
    private String employeeId;

    /**
     * 创建者
     */
    private String creater;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;

    /**
     * 更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;
}
