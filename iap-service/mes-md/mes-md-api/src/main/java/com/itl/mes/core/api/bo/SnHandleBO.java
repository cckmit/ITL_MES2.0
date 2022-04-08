package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/7/25
 * @time 16:33
 */
public class SnHandleBO implements Serializable {

    public static final String PREFIX = BOPrefixEnum.SN.getPrefix();

    private String bo;
    private String site;
    private String sn;

    public SnHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.sn = boArr[ 1 ];
    }

    public SnHandleBO(String site, String sn ){

        this.site = site;
        this.sn = sn;
        this.bo = new StringBuilder().append( PREFIX ).append( ":" ).append( site ).append( "," ).append( sn ).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getSn() {
        return sn;
    }
}
