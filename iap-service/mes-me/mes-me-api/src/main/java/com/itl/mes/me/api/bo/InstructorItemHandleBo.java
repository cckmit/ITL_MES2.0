package com.itl.mes.me.api.bo;

import com.itl.mes.me.api.constant.BOPrefixEnum;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
public class InstructorItemHandleBo {
    private static final String PREFIX = BOPrefixEnum.INSTRUCTOR_ITEM.getPrefix();

    private String bo;
    private String site;
    private String instructorItem;
    private String version;

    public InstructorItemHandleBo(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.instructorItem = split[1];
        this.version = split[2];
    }

    public InstructorItemHandleBo(String site, String instructorItem, String version) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site)
                .append(",").append(instructorItem)
                .append(",").append(version)
                .toString();
        this.site = site;
        this.instructorItem = instructorItem;
        this.version = version;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getInstructorItem() {
        return instructorItem;
    }

    public String getVersion() {
        return version;
    }
}
