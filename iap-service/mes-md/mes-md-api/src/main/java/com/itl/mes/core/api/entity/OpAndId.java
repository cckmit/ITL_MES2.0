package com.itl.mes.core.api.entity;

import lombok.Data;

@Data
public class OpAndId {
    private String operationBo;
    private String id;//在工艺路线配置表中的id值，唯一
}
