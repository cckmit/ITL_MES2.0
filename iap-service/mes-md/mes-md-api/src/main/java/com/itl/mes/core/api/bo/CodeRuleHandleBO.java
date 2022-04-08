package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class CodeRuleHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.CR.getPrefix();

    private String bo;
    private String site;
    private String codeRuleType;

    public CodeRuleHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.split(":")[1].split("," );
        this.site = boArr[ 0 ];
        this.codeRuleType = boArr[ 1 ];
    }

    public CodeRuleHandleBO(String site, String codeRuleType ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( codeRuleType ).toString();
        this.site = site;
        this.codeRuleType = codeRuleType;
    }


    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getCodeRuleType() {
        return codeRuleType;
    }
}
