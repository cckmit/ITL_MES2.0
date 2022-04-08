package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class WarehouseHandleBO implements Serializable {

    private static final String PREFIX = BOPrefixEnum.WARE_HOUSE.getPrefix();

    private String bo;
    private String site;
    private String wareHouse;

    public WarehouseHandleBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.wareHouse = split[1];
    }

    public WarehouseHandleBO(String site, String wareHouse){
           this.site=site;
           this.wareHouse=wareHouse;
           this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(wareHouse).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getWareHouse() {
        return wareHouse;
    }
}
