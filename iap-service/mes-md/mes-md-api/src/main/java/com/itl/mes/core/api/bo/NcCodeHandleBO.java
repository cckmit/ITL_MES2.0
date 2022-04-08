package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class NcCodeHandleBO implements Serializable {
    public static final long serialVersionUID = 1L;

    private static final String PREFIX = BOPrefixEnum.NC.getPrefix();

    private String bo;
    private String site;
    private String ncCode;


    public NcCodeHandleBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.ncCode = split[1];
    }

    public NcCodeHandleBO(String site, String ncCode){
        this.site =site;
        this.ncCode= ncCode;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(ncCode).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getNcCode() {
        return ncCode;
    }
}
