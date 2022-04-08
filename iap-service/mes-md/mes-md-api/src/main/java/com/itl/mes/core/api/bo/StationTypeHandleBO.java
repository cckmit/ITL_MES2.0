package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class StationTypeHandleBO implements Serializable {

    private static final String PREFIX= BOPrefixEnum.ST.getPrefix();

    private String bo;
    private String site;
    private String stationType;
   public StationTypeHandleBO(String bo){
       this.bo=bo;
       String[] split = bo.split(":")[1].split(",");
       this.site=split[0];
       this.stationType=split[1];
   }
    public StationTypeHandleBO(String site, String stationType){
         this.site=site;
         this.stationType=stationType;
         this.bo= new StringBuilder(PREFIX).append(":").append(site).append(",").append(stationType).toString();
    }
    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getStationType() {
        return stationType;
    }
}
