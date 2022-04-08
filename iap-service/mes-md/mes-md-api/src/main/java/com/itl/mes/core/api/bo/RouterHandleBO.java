package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * 工艺路线BO组装
 */
public class RouterHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.ROUTER.getPrefix();

    private String bo;
    private String site;
    private String router;
    private String version;

    public RouterHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.router = boArr[ 1 ];
        this.version = boArr[ 2 ];
    }

    public RouterHandleBO(String site, String router, String version ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( router ).append( "," ).append( version ).toString();
        this.site = site;
        this.router = router;
        this.version = version;

    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getRouter() {
        return router;
    }

    public String getVersion() {
        return version;
    }
}
