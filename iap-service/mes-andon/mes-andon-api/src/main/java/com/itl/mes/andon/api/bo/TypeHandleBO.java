package com.itl.mes.andon.api.bo;

import com.itl.mes.andon.api.constant.BOPrefixEnum;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
public class TypeHandleBO {
    private static final String PREFIX = BOPrefixEnum.AT.getPrefix();

    private String bo;
    private String site;
    private String andonType;

    public TypeHandleBO(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.andonType = split[1];
    }

    public TypeHandleBO(String site, String andonType) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site)
                .append(",").append(andonType)
                .toString();
        this.site = site;
        this.andonType = andonType;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getAndonType() {
        return andonType;
    }
}
