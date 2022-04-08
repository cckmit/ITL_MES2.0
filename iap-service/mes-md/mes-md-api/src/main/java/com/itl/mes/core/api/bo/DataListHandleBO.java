package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/6/3
 * @time 10:33
 */
public class DataListHandleBO implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PREFIX = BOPrefixEnum.DL.getPrefix();

    private String bo;
    private String dataList;

    public DataListHandleBO(String dataList){
        this.dataList =dataList;
        this.bo=PREFIX+":"+dataList;
    }


    public String getBo() {
        return bo;
    }

    public String getDataList() {
        return dataList;
    }
}
