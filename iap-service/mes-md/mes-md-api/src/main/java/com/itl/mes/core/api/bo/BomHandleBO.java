package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.entity.Bom;
import com.itl.mes.core.api.vo.BomVo;

import java.io.Serializable;

public class BomHandleBO implements Serializable {

    private static final String PREFIX = BOPrefixEnum.BOM.getPrefix();

    private String  bo;
    private String  site;
    private String  bom;
    private String  version;

    public BomHandleBO(String  bo){
        this.bo =bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.bom=split[1];
        this.version=split[2];
    }

    public BomHandleBO(String  site, String  bom, String  version){
        this.site=site;
        this.bom=bom;
        this.version=version;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(bom).append(",").append(version).toString();
    }

    public static BomVo getInstanceBomToBomVo(Bom bom ){
        BomVo bomVo = new BomVo();
        bomVo.setBo(bom.getBo());
        bomVo.setBom(bom.getBom());
        bomVo.setBomDesc(bom.getBomDesc());
        bomVo.setSite(bom.getSite());
        bomVo.setIsCurrentVersion(bom.getIsCurrentVersion());
        bomVo.setState(bom.getState());
        bomVo.setVersion(bom.getVersion());
        bomVo.setZsType(bom.getZsType());
        bomVo.setVersion(bom.getVersion());
        return bomVo;
    }

    public static String getStr(String bo){
        String s = bo.split(":")[1].split(",")[1];
        return s;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getBom() {
        return bom;
    }

    public String getVersion() {
        return version;
    }
}
