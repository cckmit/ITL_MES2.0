package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class StationHandleBO implements Serializable{

    private static final String PREFIX = BOPrefixEnum.STATION.getPrefix();

    private String bo;
    private String site;
    private String station;

    public StationHandleBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.station=split[1];
    }

    public StationHandleBO(String site, String station){
        this.bo= new StringBuilder(PREFIX).append(":").append(site).append(",").append(station).toString();
        this.site=site;
        this.station=station;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getStation() {
        return station;
    }
}
