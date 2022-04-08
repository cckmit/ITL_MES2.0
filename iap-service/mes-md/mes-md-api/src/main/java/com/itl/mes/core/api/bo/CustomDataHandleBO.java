package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class CustomDataHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.CD.getPrefix();

    private String bo;
    private String site;
    private String customDataType;
    private String cdField;

    public CustomDataHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split("," );
        this.site = boArr[ 0 ];
        this.customDataType = boArr[ 1 ];
        this.cdField = boArr[ 2 ];
    }

    public CustomDataHandleBO(String site, String customDataType, String cdField ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( customDataType ).append( "," ).append( cdField ).toString();
        this.site = site;
        this.customDataType = customDataType;
        this.cdField = cdField;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getCustomDataType() {
        return customDataType;
    }

    public String getCdField() {
        return cdField;
    }

}
