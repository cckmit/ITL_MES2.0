package com.itl.mes.me.api.bo;

import com.itl.mes.me.api.constant.BOPrefixEnum;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
public class InstructorVarHandleBo {
    private static final String PREFIX = BOPrefixEnum.INSTRUCTOR_VAR.getPrefix();

    private String bo;
    private String site;
    private String varCode;
    private String version;

    public InstructorVarHandleBo(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.varCode = split[1];
        this.version = split[2];
    }

    public InstructorVarHandleBo(String site, String varCode, String version) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site)
                .append(",").append(varCode)
                .append(",").append(version)
                .toString();
        this.site = site;
        this.varCode = varCode;
        this.version = version;
    }

    public String getBo() {
        return bo;
    }

    public String getVarCode() {
        return varCode;
    }

    public String getVersion() {
        return version;
    }
}
