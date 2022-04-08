package com.itl.mes.andon.api.bo;

import com.itl.mes.andon.api.constant.BOPrefixEnum;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
public class BoxHandlerBO {
    private static final String PREFIX = BOPrefixEnum.AB.getPrefix();

    private String bo;
    private String site;
    private String box;

    public BoxHandlerBO(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.box = split[1];
    }

    public BoxHandlerBO(String site, String box) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site)
                .append(",").append(box)
                .toString();
        this.site = site;
        this.box = box;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getBox() {
        return box;
    }
}
