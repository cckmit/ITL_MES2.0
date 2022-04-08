package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class NcGroupHandleBO implements Serializable {

    private static final String PREFIX = BOPrefixEnum.NG.getPrefix();

    private String bo;
    private String site;
    private String ncGroup;

    public NcGroupHandleBO(String bo){
        this.bo =bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.ncGroup=split[1];
    }

    public NcGroupHandleBO(String site, String ncGroup){
        this.site =site;
        this.ncGroup=ncGroup;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(ncGroup).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getNcGroup() {
        return ncGroup;
    }
}
