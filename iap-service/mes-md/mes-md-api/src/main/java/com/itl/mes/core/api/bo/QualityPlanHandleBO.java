package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

/**
 * @author 26062
 */
public class QualityPlanHandleBO {
    private String bo;
    private String site;
    private String qualityPlan;

    private static final String PREFIX = BOPrefixEnum.QP.getPrefix();


    public QualityPlanHandleBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.qualityPlan = split[1];
    }

    public QualityPlanHandleBO(String site, String qualityPlan){
        this.site =site;
        this.qualityPlan= qualityPlan;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(qualityPlan).toString();
    }


    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getQualityPlan() {
        return qualityPlan;
    }
}
