package com.itl.mes.core.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
@Data
public class SyncRouterProcess {
    @JSONField(ordinal = 1,name="nodeList")
    private List<NodeList> nodeList;
    @JSONField(ordinal = 2,name="lineList")
    private List<LineList> lineList;
    @Data
    public static class NodeList{
        @JSONField(ordinal = 1,name="id")
        private String id;
        @JSONField(ordinal = 2,name="name")
        private String name;
        @JSONField(ordinal = 3,name="operation")
        private String operation;
        @JSONField(ordinal = 4,name="type")
        private String type;
        @JSONField(ordinal = 5,name="left")
        private String left;
        @JSONField(ordinal = 6,name="top")
        private String top;
        @JSONField(ordinal = 7,name="ico")
        private String ico;
        @JSONField(ordinal = 8,name="state")
        private String state;
    }
    @Data
    public static class LineList{
        @JSONField(ordinal = 1,name="from")
        private String from;
        @JSONField(ordinal = 2,name="to")
        private String to;
    }
}
