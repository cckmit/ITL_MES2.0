package com.itl.iap.mes.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataCollectionVo {

    private String id;

    /**
     * 数据收集类型id
     */
    private String dataCollectionTypeId;

    /**
     * 数据收集类型
     */
    private String dataCollectionTypeName;

    /**
     * 数据收集类型描述
     */
    private String dataCollectionTypeRemark;

    /**
     * 数据收集名称
     */
    private String name;

    /**
     * 描述
     */
    private String remark;

    /**
     * 状态
     */
    private Integer state;

    private String stateName;
}
