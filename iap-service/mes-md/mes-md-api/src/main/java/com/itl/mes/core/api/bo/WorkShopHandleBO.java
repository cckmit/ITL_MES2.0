package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * 车间BO组装
 */
public class WorkShopHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;
    public static final String PREFIX = BOPrefixEnum.WS.getPrefix();

    private String bo;
    private String site;
    private String workShop;

    public WorkShopHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.workShop = boArr[ 1 ];
    }

    public WorkShopHandleBO(String site, String workShop ){

        this.site = site;
        this.workShop = workShop;
        this.bo = new StringBuilder().append( PREFIX ).append( ":" ).append( site ).append( "," ).append( workShop ).toString();

    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getWorkShop() {
        return workShop;
    }

}
