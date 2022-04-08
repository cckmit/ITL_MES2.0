package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * lov组件条目
 * @author 胡广
 * @version 1.0
 * @name Lov
 * @description
 * @date 2019-08-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_mes_lov_entry")
public class LovEntry implements Serializable {

    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;
    @TableField("lovId")
    private String lovId;
    @TableField("field")
    private String field;
    @TableField("label")
    private String label;
    @TableField("name")
    private String name;
    @TableField("searchable")
    private Boolean searchable;

    @TableField("displayable")
    private Boolean displayable;
    @TableField("defaultValue")
    private String defaultValue;
    @TableField("sort")
    private Long sort;
    @TableField("columnWidth")
    private Integer columnWidth;
    @TableField("fieldType")
    private String fieldType;
    @TableField("filedName")
    private String filedName;
    /**
     * 启用标志
     */
 //   @Column(updatable = false,columnDefinition="char(1) default 'Y'")
    @TableField(value = "enabledFlag")
    private String enabledFlag;

}
