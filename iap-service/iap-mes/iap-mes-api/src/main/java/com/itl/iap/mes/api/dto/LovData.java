package com.itl.iap.mes.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author 胡广
 * @version 1.0
 * @name LovData
 * @description
 * @date 2019-08-21
 */
@Data
public class LovData {
    private String id;
    private String code;
    private String name;
    private String valueField;
    private String textField;
    private String title;
    private BigDecimal width;
    private Integer page;
    private Integer pageSize;
    private Long total;
    private String placeHolder;
    //JSON格式，搜索参数, 使用form-create进行渲染
    private String parameter;
    //直接使用SQL取数
    private String sqlStatement;
    //通过Mybatis的方法取数
    private String sqlId;
    private String description;
    //通过服务获取数据
    private String serviceExpression;
    //通过rest API 获取数据
    private String apiUrl;

    private List<LovEntryData> entries;

    //搜索参数
    private Map<String,String> searchParams;

    //查询结果集
    private List<Map<String,Object>> searchResult;

    //用于渲染查询form表单
    private List<FormCreateRule> formCreateRules;

    //访问SQL服务地址(前台先从Lov服务器获取一下配置，然后调用对应的服务查询数据)
    private String sqlServiceUrl;

    //SQL类型 {SQL,SERVICE,API}
    private String sqlTypeCode;

    //参数类型 [JSON,MAP]
    private String paramTypeCode;


}
