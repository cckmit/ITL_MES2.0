package com.itl.iap.mes.api.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("label_params")
public class LabelEntityParams {
    @TableId(type = IdType.UUID)
    private String id;

    @TableField( "code")
    private String code;

    @TableField( "name")
    private String name;

    @TableField( "isFile")
    private Integer isFile;

    @TableField( "labelEntityId")
    private String  labelEntityId;

    @TableField( "table_name")
    private String  tableName;

    @TableField( "field_name")
    private String  fieldName;
}
