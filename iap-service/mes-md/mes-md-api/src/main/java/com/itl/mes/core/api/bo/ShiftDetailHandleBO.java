package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class ShiftDetailHandleBO implements Serializable {

    private static final String PREFIX = BOPrefixEnum.SD.getPrefix();

    private static final String shiftPrefix = BOPrefixEnum.SHIFT.getPrefix();

    private String bo;
    private  String site;
    private String shift;
    private String seq;
    private String shiftBO;

    public ShiftDetailHandleBO(String bo){
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.shift=split[1];
        this.seq=split[2];
        this.shiftBO= new StringBuilder(shiftPrefix).append(":").append(split[0]).append(",").append(split[1]).toString();
    }

    public ShiftDetailHandleBO(String site, String shift, String seq){
        this.site=site;
        this.shift=shift;
        this.seq=seq;
        this.shiftBO=new StringBuilder(shiftPrefix).append(":").append(site).append(",").append(shift).toString();
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(shift).append(",").append(seq).toString();
    }

    public ShiftDetailHandleBO(String shiftBO, String seq){
        String[] split = shiftBO.split(":")[1].split(",");
        this.site = split[0];
        this.shift = split[1];
        this.seq=seq;
        this.shiftBO=shiftBO;
        this.bo=new StringBuilder(PREFIX).append(":").append(split[0]).append(",").append(split[1]).append(",").append(seq).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getShift() {
        return shift;
    }

    public String getSeq() {
        return seq;
    }

    public String getShiftBO() {
        return shiftBO;
    }
}
