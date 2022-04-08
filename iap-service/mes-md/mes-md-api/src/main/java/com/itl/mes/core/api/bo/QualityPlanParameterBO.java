package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

public class QualityPlanParameterBO {
    private String bo;
    private String site;
    private String parameterDesc;

    private static final String PREFIX = BOPrefixEnum.QPP.getPrefix();


    public QualityPlanParameterBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.parameterDesc = split[1];
    }

    public QualityPlanParameterBO(String site, String parameterDesc){
        this.site =site;
        this.parameterDesc= parameterDesc;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(parameterDesc).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getParameterDesc() {
        return parameterDesc;

    }
}
