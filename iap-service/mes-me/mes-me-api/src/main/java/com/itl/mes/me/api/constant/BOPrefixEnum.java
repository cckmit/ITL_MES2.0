package com.itl.mes.me.api.constant;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
public enum BOPrefixEnum {
    INSTRUCTOR("INSTRUCTOR", "m_instructor"),
    INSTRUCTOR_ITEM("II", "m_instructor_item"),
    INSTRUCTOR_VAR("IV", "m_instructor_var");

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
