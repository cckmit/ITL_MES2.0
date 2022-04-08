package com.itl.mes.andon.api.bo;

import com.itl.mes.andon.api.constant.BOPrefixEnum;

public class AndonHandleBo {

    private static final String PREFIX = BOPrefixEnum.AA.getPrefix();

    private String bo;
    private String site;
    private String andon;

    public AndonHandleBo(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.andon = split[1];
    }

    public AndonHandleBo(String site, String andon) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site)
                .append(",").append(andon)
                .toString();
        this.site = site;
        this.andon = andon;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getAndon() {
        return andon;
    }

}
