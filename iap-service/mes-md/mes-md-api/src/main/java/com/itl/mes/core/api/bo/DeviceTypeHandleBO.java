package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

/**
 * @author sky,
 * @date 2019/6/17
 * @time 14:15
 */
public class DeviceTypeHandleBO {
    public static final long serialVersionUID=1L;

    public static final String PREFIX = BOPrefixEnum.RT.getPrefix();

    private String bo;
    private String site;
    private String deviceType;

    public DeviceTypeHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.deviceType = boArr[ 1 ];

    }

    public DeviceTypeHandleBO(String site, String deviceType ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( deviceType ).toString();
        this.site = site;
        this.deviceType = deviceType;


    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getDeviceType() {
        return deviceType;
    }
}
