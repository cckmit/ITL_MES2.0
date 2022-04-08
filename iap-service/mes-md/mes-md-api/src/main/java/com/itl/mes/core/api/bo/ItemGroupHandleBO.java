package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class ItemGroupHandleBO implements Serializable {

    public static final long serialVersionUID=1L;

    public static final String PREFIX = BOPrefixEnum.IG.getPrefix();

    private String bo;
    private String site;
    private String itemGroup;

    public ItemGroupHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.itemGroup = boArr[ 1 ];

    }

    public ItemGroupHandleBO(String site, String itemGroup ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( itemGroup ).toString();
        this.site = site;
        this.itemGroup = itemGroup;

    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getItemGroup() {
        return itemGroup;
    }
}
