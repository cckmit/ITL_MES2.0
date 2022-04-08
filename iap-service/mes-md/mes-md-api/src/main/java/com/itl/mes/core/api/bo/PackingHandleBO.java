package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class PackingHandleBO implements Serializable {

    private static final String PREFIX = BOPrefixEnum.PK.getPrefix();

    private String bo;

    private String site;

    private String packName;

    public PackingHandleBO(String bo){
        String[] split = bo.split(":")[1].split(",");
        this.bo = bo;
        this.site = split[0];
        this.packName = split[1];
    }

    public PackingHandleBO(String site, String packName){
        this.site = site;
        this.packName = packName;
        this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(packName).toString();
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getPackName() {
        return packName;
    }
}
