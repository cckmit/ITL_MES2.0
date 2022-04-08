package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

public class InspectTypeHandleBO {
    public static final long serialVersionUID = 1L;

    private String bo;
    private String site;
    private String inspectType;

    private static final String PREFIX = BOPrefixEnum.IT.getPrefix();

    public InspectTypeHandleBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.inspectType = split[1];
    }

    public InspectTypeHandleBO(String site, String inspectType){
        this.site =site;
        this.inspectType= inspectType;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(inspectType).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getInspectType() {
        return inspectType;
    }
}
