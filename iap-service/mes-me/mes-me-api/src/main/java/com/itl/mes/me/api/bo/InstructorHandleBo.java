package com.itl.mes.me.api.bo;

import com.itl.mes.me.api.constant.BOPrefixEnum;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
public class InstructorHandleBo {
    private static final String PREFIX = BOPrefixEnum.INSTRUCTOR.getPrefix();

    private String bo;
    private String site;
    private String instructor;
    private String version;

    public InstructorHandleBo(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.instructor = split[1];
        this.version = split[2];
    }

    public InstructorHandleBo(String site, String instructor, String version) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site)
                .append(",").append(instructor)
                .append(",").append(version)
                .toString();
        this.site = site;
        this.instructor = instructor;
        this.version = version;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getVersion() {
        return version;
    }
}
