package com.itl.mes.core.api.entity;

import lombok.Data;

@Data
public class OperationTime {
    //总工序数
    private String opCounts;
    //时长最长工序
    private String theLongestOp;
    //时长最长工时
    private String theLongestTime;
}
