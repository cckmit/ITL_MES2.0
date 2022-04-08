package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * @author 崔翀赫
 * @date 2021/1/26$
 * @since JDK1.8
 */
public class MeSfcHandleBO implements Serializable {
    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.SFC.getPrefix();

    private String bo;
    private String site;
    private String sfc;

    public MeSfcHandleBO(String bo) {
        this.bo = bo;
        String[] boArr = bo.substring(PREFIX.length() + 1).split(",");
        this.site = boArr[0];
        this.sfc = boArr[1];
    }

    public MeSfcHandleBO(String site, String sfc) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(sfc).toString();
        this.site = site;
        this.sfc = sfc;
    }

    public String getBo() {
        return bo;
    }


    public String getSite() {
        return site;
    }


    public String getSfc() {
        return sfc;
    }

}
