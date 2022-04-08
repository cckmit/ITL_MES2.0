package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class CarrierItemHandleBO implements Serializable {
    private static final String PREFIX = BOPrefixEnum.CI.getPrefix();
    private static final String CT = BOPrefixEnum.CT.getPrefix();

    private String bo;

    private String site;

    private String carrierType;

    private String seq;

    private String carrierTypeBO;

    public CarrierItemHandleBO(String bo){
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.carrierType = split[1];
        this.seq = split[2];
        this.carrierTypeBO = new StringBuilder(CT).append(":").append(split[0]).append(":").append(split[1]).toString();
    }

    public CarrierItemHandleBO(String site, String carrierType, String seq){
        this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(carrierType).append(",").append(seq).toString();
        this.carrierType = carrierType;
        this.site = site;
        this.seq = seq;
        this.carrierTypeBO = new StringBuilder(CT).append(":").append(site).append(":").append(carrierType).toString();
    }

    public CarrierItemHandleBO(String carrierTypeBO, String seq){
        this.carrierTypeBO = carrierTypeBO;
        this.seq = seq ;
        String[] split = carrierTypeBO.split(":")[1].split(",");
        this.site = split[0];
        this.carrierType = split[1];
        this.bo = new StringBuilder(PREFIX).append(":").append(split[0]).append(",").append(split[1]).append(",").append(seq).toString();
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public static String getCT() {
        return CT;
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

    public String getSeq() {
        return seq;
    }

    public String getCarrierTypeBO() {
        return carrierTypeBO;
    }
}
