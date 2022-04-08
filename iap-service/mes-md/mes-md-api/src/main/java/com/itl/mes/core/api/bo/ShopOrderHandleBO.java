package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class ShopOrderHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.SO.getPrefix();

    private String bo;
    private String site;
    private String shopOrder;

    public ShopOrderHandleBO(String bo ) {
        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.shopOrder = boArr[ 1 ];
    }

    public ShopOrderHandleBO(String site, String shopOrder ) {
        this.site = site;
        this.shopOrder = shopOrder;
        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( shopOrder ).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getShopOrder() {
        return shopOrder;
    }
}
