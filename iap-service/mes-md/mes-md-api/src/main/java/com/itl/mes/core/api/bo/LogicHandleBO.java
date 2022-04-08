package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class LogicHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.LOGIC.getPrefix();

    private String bo;
    private String logicNo;
    private String version;

    public LogicHandleBO(String bo) {
        this.bo = bo;
        String[] boArr = bo.substring(PREFIX.length() + 1).split(",");
        this.logicNo = boArr[0];
        this.version = boArr[1];
    }

    public LogicHandleBO(String logicNo, String version) {
        this.bo = PREFIX + ":" + logicNo + "," + version;
        this.logicNo = logicNo;
        this.version = version;
    }

    public String getBo() {
        return this.bo;
    }

    public String getLogicNo() {
        return this.logicNo;
    }

    public String getVersion() {
        return this.version;
    }
}
