package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class CarrierTypeHandleBO implements Serializable {
    private static final String PREFIX = BOPrefixEnum.CT.getPrefix();

    private String bo;
    private String site;
    private String carrierType;

    public CarrierTypeHandleBO(String bo){
            this.bo = bo ;
            String[] split = bo.split(":")[1].split(",");
            this.site = split[0];
            this.carrierType = split[1];
    }

    public CarrierTypeHandleBO(String site, String carrierType){
            this.site = site;
            this.carrierType = carrierType;
            this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(carrierType).toString();
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

    public String getCarrierType() {
        return carrierType;
    }
}
