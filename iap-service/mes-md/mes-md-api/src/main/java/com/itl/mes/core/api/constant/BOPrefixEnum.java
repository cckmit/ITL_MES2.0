package com.itl.mes.core.api.constant;

/**
 * 表BO前缀,构造函数: 前缀("前缀","表名(去掉第一段下划线及字母)")
 *
 */
public enum BOPrefixEnum {

    SN( "SN","SN" ),
    ITEM( "ITEM","ITEM" ),
    IG( "IG","ITEM_GROUP" ),
    OP( "OP","OPERATION" ),
    OPS("OPS","D_DY_WORKSTATION"),
    WS("WS","WORK_SHOP"  ),
    PL( "PL","PRODUCT_LINE" ),
    CR( "CR","CODE_RULE" ),
    CRI("CRI","CODE_RULE_ITEM" ),
    CD("CD","CUSTOM_DATA" ),
    CDT( "CDT","CUSTOM_DATA_TYPE" ),
    ROUTER( "ROUTER","ROUTER" ),
    RS( "RS","ROUTER_STEP" ),
    RNS( "RNS","ROUTER_NEXT_STEP" ),
    RN( "RN" ,"ROUTER_NODE" ),
    RL( "RL", "ROUTER_LINK" ),
    SO( "SO","SHOP_ORDER" ),
    STATUS( "STATE","STATUS" ),
    STATION( "STATION","STATION" ),
    CUSTOMER( "CUSTOMER","CUSTOMER" ),
    CO( "CO","CUSTOMER_ORDER" ),
    CUI("CUI","CUSTOMER_ORDER_ITEM"),
    DL( "DL","DATA_LIST" ),
    DG( "DG","DC_GROUP" ),
    DP("DP","DC_PARAMETER"),
    DGA("DGA","DC_GROUP_ATTACH"),
    RES( "RES","DEVICE" ),
    RT( "RT","DEVICE_TYPE" ),
    ST("ST","STATION_TYPE" ),
    TEAM("TEAM","TEAM"),
    VC("VC","VENDOR_COMPONENT" ),
    VENDOR("VENDOR","VENDOR"),
    WARE_HOUSE("WAREHOUSE","WARE_HOUSE" ),
    NC("NC","NC_CODE" ),
    NG("NG","NC_GROUP" ),
    SD("SD","SHIFT_DETAIL" ),
    SHIFT("SHIFT","SHIFT"),
    BC("BC","BOM_COMPONENT" ),
    BOM("BOM","BOM"),
    PK("PK","PACKING"),
    PKL("PKL","PACK_LEVEL"),
    PKD("PKD","PACK_DATA "),
    RDS("RDS","ROUTER_DONE_STEP"),
    RHS("RHS","ROUTER_HOLD_STEP"),
    RRS("RRS","ROUTER_RETURN_STEP"),
    RSS("RSS","ROUTER_SCRAP_STEP"),
    RSO("RSO","ROUTER_STEP_OPERATION"),
    SFC("SFC","me_sfc"),
    SR("SR","SFC_ROUTER"),
    SS("SS","SFC_STEP"),
    PKDI("PKDI","PACK_DATA_ITEM"),
    CT("CT","CARRIER_TYPE"),
    CI("CI","CARRIER_ITEM"),
    IT("InspectTypeBO","INSPECT_TYPE"),
    QP("QualityPlanBO","QUALITY_PLAN"),
    QPP("QppBO","QUALITY_PLAN_PARAMETER"),
    AE("AttachedBO","M_ATTACHED"),
    LOGIC("LOGIC","LOGIC"),
    InspectTaskBO("InspectTaskBO","INSPECT_TASK"),
    CARRIER("CARRIER","CARRIER");



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
