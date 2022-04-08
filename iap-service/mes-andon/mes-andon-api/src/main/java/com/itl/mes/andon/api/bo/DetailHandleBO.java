package com.itl.mes.andon.api.bo;

import com.itl.mes.andon.api.constant.BOPrefixEnum;

public class DetailHandleBO {
    private static final String PREFIX = BOPrefixEnum.AT.getPrefix();

    private String bo;
    private String site;
    private String desc;

    public DetailHandleBO(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.desc = split[1];
    }

    public DetailHandleBO(String site, String desc) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site)
                .append(",").append(desc)
                .toString();
        this.site = site;
        this.desc = desc;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getDesc() {
        return desc;
    }
}
