package com.itl.mes.core.api.bo;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import io.micrometer.core.instrument.util.StringUtils;

import java.io.Serializable;

public class BomComponentHandleBO implements Serializable{

    private static final String PREFIX = BOPrefixEnum.BC.getPrefix();

    private String bo;
    private String site;
    private String bom;
    private String item;
    private String version;

    public BomComponentHandleBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.bom=split[1];
        this.item=split[2];
        this.version=split[3];
    }

    public BomComponentHandleBO(String site, String bom, String item, String version) throws CommonException {
        if(StringUtils.isBlank(item))throw new CommonException("组件不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        this.site=site;
        this.bom=bom;
        this.item=item;
        this.version=version;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(bom).append(",").append(item).append(",").append(version).toString();
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

    public String getItem() {
        return item;
    }

    public String getVersion() {
        return version;
    }
}
