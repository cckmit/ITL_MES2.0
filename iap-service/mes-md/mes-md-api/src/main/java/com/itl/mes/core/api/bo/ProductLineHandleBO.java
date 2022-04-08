package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class ProductLineHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.PL.getPrefix();

    private String bo;
    private String site;
    private String productLine;

    public ProductLineHandleBO(String bo ) {
        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.productLine = boArr[ 1 ];
    }

    public ProductLineHandleBO(String site, String productLine ) {
        this.site = site;
        this.productLine = productLine;
        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( productLine ).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getProductLine() {
        return productLine;
    }
}
