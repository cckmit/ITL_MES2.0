package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class ItemHandleBO implements Serializable {

    public static final long serialVersionUID=1L;

    public static final String PREFIX = BOPrefixEnum.ITEM.getPrefix();

    private String bo;
    private String site;
    private String item;
    private String version;

    public ItemHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.item = boArr[ 1 ];
        this.version = boArr[ 2 ];

    }

    public ItemHandleBO(String site, String item, String version ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( item ).append( "," ).append( version ).toString();
        this.site = site;
        this.item = item;
        this.version = version;

    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getItem() {
        return item;
    }

    public String getVersion() {
        return version;
    }
}
