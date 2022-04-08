package com.itl.mes.pp.api.entity.scheduleplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 班制表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_class_system")
public class ClassSystemEntity {


    @TableId(value = "ID",type = IdType.UUID)
    private String id;

    @TableField( "CODE")
    private String code;

    @TableField( "NAME")
    private String name;

    @TableField( "IS_DEFAULT")
    private Integer isDefault;


}
