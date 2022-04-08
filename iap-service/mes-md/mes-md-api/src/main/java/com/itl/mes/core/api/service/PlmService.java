package com.itl.mes.core.api.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.PlmDto;
import com.itl.mes.core.api.entity.WorkStation;

import java.util.List;

public interface PlmService {
    void setErrMsg(String errMsg);

    String getErrMsg();

    String getWorkStepNo(PlmDto plmDto) throws CommonException;
    List<WorkStation> getAllWorkStation(String item, String version) throws CommonException;
}
