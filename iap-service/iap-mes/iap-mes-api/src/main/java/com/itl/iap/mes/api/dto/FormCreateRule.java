package com.itl.iap.mes.api.dto;

import lombok.Data;

/**
 * 前端 form-create rule
 * @author 胡广
 * @version 1.0
 * @name FormCreateRule
 * @description
 * @date 2019-08-22
 */
@Data
public class FormCreateRule {
    private String type;
    private String title;
    private String field;
    private String value;
}
