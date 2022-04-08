package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * lov组件
 * @author 胡广
 * @version 1.0
 * @name Lov
 * @description
 * @date 2019-08-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_mes_lov")
public class Lov implements Serializable {

    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;
    @TableField("code")
    private String code;
    @TableField("name")
    private String name;
    @TableField("valueField")
    private String valueField;
    @TableField("textField")
    private String textField;
    @TableField("title")
    private String title;
    @TableField("width")
    private Integer width;
    @TableField("pageSize")
    private Long pageSize;
    @TableField("placeHolder")
    private String placeHolder;
    //JSON格式，搜索参数, 使用form-create进行渲染, 与entries可采取 覆盖，合并，和保留一个的方式，默认覆盖。
    @TableField("parameter")
    private String parameter;
    //直接使用SQL取数
    @TableField("sqlStatement")
    private String sqlStatement;
    //通过服务获取数据
    @TableField("serviceExpression")
    private String serviceExpression;
    //通过rest API 获取数据
    @TableField("apiUrl")
    private String apiUrl;

    //访问SQL服务地址(前台先从Lov服务器获取一下配置，然后调用对应的服务查询数据)
    @TableField("sqlServiceUrl")
    private String sqlServiceUrl;
    @TableField("description")
    private String description;
    //    @OneToMany(mappedBy = "lovId",cascade=CascadeType.DETACH,fetch=FetchType.LAZY)
    @TableField(exist = false)
    private List<LovEntry> entries;

    //SQL类型 {SQL,SERVICE,API}
    @TableField("sqlTypeCode")
    private String sqlTypeCode;

    //参数类型 [JSON,MAP]
    @TableField("paramTypeCode")
    private String paramTypeCode;
    /**
     * 启用标志
     */
   // @Column(name="enabledFlag",updatable = false,columnDefinition="char(1) default 'Y'")
    @TableField("enabledFlag")
    private String enabledFlag;
}
