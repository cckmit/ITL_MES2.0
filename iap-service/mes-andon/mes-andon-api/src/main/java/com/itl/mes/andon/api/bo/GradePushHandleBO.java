package com.itl.mes.andon.api.bo;

import java.io.Serializable;

public class GradePushHandleBO implements Serializable {

    private String bo;
    private String site;
    private String type;
    private int andonGrade;

    public GradePushHandleBO(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.type = split[1];
        this.andonGrade = Integer.parseInt(split[2]);
    }

    public GradePushHandleBO(String site,String type, int andonGrade) {

        this.site = site;
        this.andonGrade = andonGrade;

        this.bo =
                new StringBuilder("GP")
                        .append(":")
                        .append(site)
                        .append(",")
                        .append(type)
                        .append(",")
                        .append(andonGrade)
                        .toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public int getAndonGrade() {
        return andonGrade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
