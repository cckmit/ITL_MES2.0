package com.itl.iap.mes.api.entity.parameter;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author liuchenghao
 * @date 2020/10/22 14:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName( "sys_parameter")
public  class ParameterEntity {




    @TableId(value = "PARAMETER_ID",type = IdType.UUID)
    private String parameterId;

    /**
     * 参数名称
     */
    @TableField( "NAME")
    private String name;

    /**
     * 参数键
     */
    @TableField( "PARAMETER_KEY")
    private String parameterKey;


    /**
     * 参数值
     */
    @TableField( "PARAMETER_VALUE")
    private String parameterValue;


    /**
     * 参数编码
     */
    @TableField( "CODE")
    private String code;


    /**
     * 参数类型（取字典表对应参数类型的值）
     */
    @TableField( "TYPE")
    private String type;


    /**
     * 参数详细类型（取字典表对应详细参数类型的值）
     */
    @TableField( "DETAIL_TYPE")
    private String detailType;


    /**
     * 参数状态
     */
    @TableField( "STATE")
    private Integer state;



}
