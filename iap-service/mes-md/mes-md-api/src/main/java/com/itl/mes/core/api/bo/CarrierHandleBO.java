package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class CarrierHandleBO implements Serializable {
     private static final String  PREFIX = BOPrefixEnum.CARRIER.getPrefix();

     private String bo;
     private String site;
     private String carrier;

     public CarrierHandleBO(String bo){
         this.bo = bo;
         String[] split = bo.split(":")[1].split(",");
         this.site = split[0];
         this.carrier = split[1];
     }

    public CarrierHandleBO(String site, String carrier){
         this.site = site ;
         this.carrier = carrier ;
         this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(carrier).toString();
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

    public String getCarrier() {
        return carrier;
    }
}
