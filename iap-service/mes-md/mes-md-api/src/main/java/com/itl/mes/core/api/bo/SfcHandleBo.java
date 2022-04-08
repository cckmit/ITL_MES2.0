package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;
public class SfcHandleBo {
    public static final String PREFIX = BOPrefixEnum.SFC.getPrefix();

    private String bo;
    private String site;
    private String sfc;

    public SfcHandleBo(String bo) {
        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1).split(",");
        this.site = boArr[0];
        this.sfc=boArr[1];
    }

    public SfcHandleBo(String site, String sfc ){

        this.site = site;
        this.sfc = sfc;
        this.bo = new StringBuilder().append( PREFIX ).append( ":" ).append( site ).append( "," ).append( sfc ).toString();
    }


    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getSfc() {
        return sfc;
    }
}
