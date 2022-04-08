package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class ShiftHandleBO implements Serializable {

    private static final String PREFIX = BOPrefixEnum.SHIFT.getPrefix();

    private String bo;
    private String site;
    private String shift;

    public ShiftHandleBO(String bo){
           this.bo=bo;
           String[] split = bo.split(":")[1].split(",");
           this.site = split[0];
           this.shift = split[1];
    }

    public ShiftHandleBO(String site, String shift){
            this.site = site;
            this.shift = shift;
            this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(shift).toString();
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
}
