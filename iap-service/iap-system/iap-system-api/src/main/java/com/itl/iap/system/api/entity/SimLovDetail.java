package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EbenChan
 * @date 2020/11/21 17:51
 **/
@Data
@Accessors(chain = true)
@TableName("sim_lov_detail")
public class SimLovDetail extends BaseModel {
    private static final long serialVersionUID = 543915602469029960L;
    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * lovId
     */
    @TableField("lov_id")
    private String lovId;
    /**
     * 字段名称
     */
    @TableField("label")
    private String label;
    /**
     * 字段属性
     */
    @TableField("prop")
    private String prop;
    /**
     * 字段类型：0-查询字段；1-表格列字段
     */
    @TableField("type")
    private Integer type;
    /**
     * 查询字段显示类型：select/input/date
     */
    @TableField("type_code")
    private String typeCode;
    /**
     * select查询字段绑定字典编码
     */
    @TableField("dict_code")
    private String dictCode;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
}
