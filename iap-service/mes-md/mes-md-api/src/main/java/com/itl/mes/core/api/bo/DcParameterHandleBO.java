package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/6/4
 * @time 10:23
 */
public class DcParameterHandleBO implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PREFIX = BOPrefixEnum.DP.getPrefix();

    private String bo;
    private String site;
    private String dcGroupBo;
    private String paramName;

    public DcParameterHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.dcGroupBo = boArr[ 1 ];
        this.paramName = boArr[ 2 ];
    }

    public DcParameterHandleBO(String site, String dcGroupBo, String paramName){
        this.site =site;
        this.dcGroupBo = dcGroupBo;
        this.paramName = paramName;
        this.bo=PREFIX+":"+site+","+dcGroupBo+","+paramName;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getDcGroupBo() {
        return dcGroupBo;
    }

    public String getParamName() {
        return paramName;
    }
}
