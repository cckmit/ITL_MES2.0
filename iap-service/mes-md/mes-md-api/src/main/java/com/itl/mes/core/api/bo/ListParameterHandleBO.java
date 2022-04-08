package com.itl.mes.core.api.bo;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/6/3
 * @time 11:21
 */
public class ListParameterHandleBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bo;
    private String dataListBo;
    private String fieldValue;

    public ListParameterHandleBO(String dataListBo, String fieldValue){

        String[] dataList = dataListBo.substring(dataListBo.indexOf("DL:") + 3).split(",");
        String data = dataList[0];

        this.fieldValue = fieldValue;

        this.bo="LP:"+data+","+fieldValue;
    }

    public String getBo() {
        return bo;
    }

    public String getDataListBo() {
        return dataListBo;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
