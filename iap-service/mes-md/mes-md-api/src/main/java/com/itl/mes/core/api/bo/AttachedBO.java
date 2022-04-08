package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

public class AttachedBO {
    public static final long serialVersionUID = 1L;

    private String bo;
    private String site;
    private String qualityPlanBO;
    private Integer seq;
    private Integer attachedKey;

    private static final String PREFIX = BOPrefixEnum.AE.getPrefix();

    public AttachedBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.qualityPlanBO = split[1];
        this.seq = Integer.getInteger(split[2]);
        this.attachedKey = Integer.getInteger(split[3]);

    }

    public AttachedBO(String site, String qualityPlanBO, Integer seq, Integer attachedKey){
        this.site =site;
        this.qualityPlanBO= qualityPlanBO;
        this.seq = seq;
        this.attachedKey = attachedKey;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(qualityPlanBO).append(",").append(seq).append(",").append(attachedKey).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getQualityPlanBO() {
        return qualityPlanBO;
    }

    public Integer getSeq() {
        return seq;
    }

    public Integer getAttachedKey() {
        return attachedKey;
    }
}
