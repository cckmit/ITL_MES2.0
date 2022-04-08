package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class OperationHandleBO implements Serializable{

    private static final String  PREFIX = BOPrefixEnum.OP.getPrefix();

    private String bo;
    private String site;
    private String operation;
    private String version;

    public OperationHandleBO(String bo){
       this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site =split[0];
        this.operation =split[1];
        this.version=split[2];
    }

    public OperationHandleBO(String site, String operation, String version){
         this.site =site;
         this.operation = operation;
         this.version =version;
         this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(operation).append(",").append(version).toString();
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

    public String getVersion() {
        return version;
    }
}
