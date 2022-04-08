package com.itl.mes.andon.api.constant;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
public enum BOPrefixEnum {
    AT("AT","andon_type"),
    AG("AG","andon_grade"),
    AP("AP","andon_push"),
    AB("AB", "andon_box"),
    AA("AA","andon_andon");

    private String table;
    private String prefix;

    BOPrefixEnum(String prefix, String table ){
        this.prefix = prefix;
        this.table = table;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getTable() {
        return table;
    }
}
