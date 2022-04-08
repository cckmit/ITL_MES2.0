package com.itl.iap.mes.api.dto;

import lombok.Data;

/**
 * @author 胡广
 * @version 1.0
 * @name LovEntryData
 * @description
 * @date 2019-08-22
 */
@Data
public class LovEntryData {
    private String id;
    private String lovId;
    private String field;
    private String label;
    private String name;
    private Boolean searchable;
    private Boolean displayable;
    private String defaultValue;
    private Long sort;
    /**
     * 列宽
     */
    private double columnWidth;
    private String fieldType;
    private String filedName;

}
