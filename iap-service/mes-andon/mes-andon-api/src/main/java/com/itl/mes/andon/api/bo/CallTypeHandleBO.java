package com.itl.mes.andon.api.bo;

import com.itl.mes.andon.api.constant.BOPrefixEnum;

public class CallTypeHandleBO {
    private static final String PREFIX = BOPrefixEnum.AT.getPrefix();

    private String bo;
    private String site;
    private String workshop;
    private String andonType;

    public CallTypeHandleBO(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.workshop = split[1];
        this.andonType = split[2];
    }

    public CallTypeHandleBO(String site, String workshop, String andonType) {
        this.bo = new StringBuilder("CT").append(":").append(site)
                .append(",").append(workshop)
                .append(",").append(andonType)
                .toString();
        this.site = site;
        this.workshop = workshop;
        this.andonType = andonType;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getWorkshop() {
        return workshop;
    }

    public String getAndonType() {
        return andonType;
    }
}
