package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class WorkStationHandleBO implements Serializable {

    private static final String  PREFIX = BOPrefixEnum.OPS.getPrefix();

    private String bo;
    private String site;
    private String operation;
    private String workStation;
    private String version;

    public WorkStationHandleBO(String bo){
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site =split[0];
        this.operation =split[1];
        this.workStation=split[2];
    }

    public WorkStationHandleBO(String site, String operation,String workStation){
        this.site =site;
        this.workStation = workStation;
        this.operation = operation;
        this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(operation).append(",").append(workStation).toString();
    }

    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getSite() {
        return site;
    }

    public String getOperation() {
        return operation;
    }

    public String getWorkStation(){return workStation;}

}
