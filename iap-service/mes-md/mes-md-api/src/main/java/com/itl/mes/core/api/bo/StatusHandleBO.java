package com.itl.mes.core.api.bo;

import java.io.Serializable;

public class StatusHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = "STATE";

    private String bo;
    private String site;
    private String state;

    public StatusHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.state = boArr[ 1 ];
    }

    public StatusHandleBO(String site, String state ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( state ).toString();
        this.site = site;
        this.state = state;

    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getState() {
        return state;
    }
}
