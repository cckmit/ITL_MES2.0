package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

public class TeamHandleBO implements Serializable {

    private static final String PREFIX = BOPrefixEnum.TEAM.getPrefix();

    private String bo;
    private String site;
    private String team;

    public TeamHandleBO(String bo){
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.team = split[1];
    }

    public TeamHandleBO(String site, String team){
        this.site = site;
        this.team = team;
        this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(team).toString();
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getTeam() {
        return team;
    }
}
