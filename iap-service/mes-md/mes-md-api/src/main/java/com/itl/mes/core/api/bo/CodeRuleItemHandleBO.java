package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class CodeRuleItemHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.CRI.getPrefix();

    private String bo;
    private String site;
    private String codeRuleBo;
    private String seq;

    public CodeRuleItemHandleBO(String bo ){

        this.bo = bo;
        this.site = bo.substring( PREFIX.length()+1,bo.indexOf( "," ) );
        this.codeRuleBo = bo.substring( bo.indexOf( "," )+1,bo.lastIndexOf( "," ) );
        this.seq = bo.substring( bo.lastIndexOf( "," )+1 );
    }



    public CodeRuleItemHandleBO(String site, String codeRuleBo, String seq ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( codeRuleBo ).append( "," ).append( seq ).toString();
        this.site = site;
        this.codeRuleBo = codeRuleBo;
        this.seq = seq;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getCodeRuleBo() {
        return codeRuleBo;
    }

    public String getSeq() {
        return seq;
    }
}
