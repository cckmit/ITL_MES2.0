package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/8/31
 * @time 15:14
 */
public class InspectTaskHandleBO implements Serializable {

    public static final String PREFIX = BOPrefixEnum.InspectTaskBO.getPrefix();

    private String bo;
    private String site;
    private String inspectTask;

    public InspectTaskHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.inspectTask = boArr[ 1 ];
    }

    public InspectTaskHandleBO(String site, String inspectTask ){

        this.site = site;
        this.inspectTask = inspectTask;
        this.bo = new StringBuilder().append( PREFIX ).append( ":" ).append( site ).append( "," ).append( inspectTask ).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getInspectTask() {
        return inspectTask;
    }
}
