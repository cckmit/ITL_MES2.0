package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author EbenChan
 * @date 2020/11/21 18:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SimLovDetailDto implements Serializable {
    private static final long serialVersionUID = -22245589193251740L;
    //分页对象
    private Page page;
    /**
     * id
     */
    private String id;
    /**
     * lovId
     */
    private String lovId;
    /**
     * 字段名称
     */
    private String label;
    /**
     * 字段属性
     */
    private String prop;
    /**
     * 字段类型：0-查询字段；1-表格列字段
     */
    private Integer type;
    /**
     * 查询字段显示类型：select/input/date
     */
    private String typeCode;
    /**
     * select查询字段绑定字典编码
     */
    private String dictCode;

    /**
     * 排序
     */
    private Integer sort;
}
