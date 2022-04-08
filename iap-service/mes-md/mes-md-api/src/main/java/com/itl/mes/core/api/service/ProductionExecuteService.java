package com.itl.mes.core.api.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.OutStationDto;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.vo.SfcVo;

public interface ProductionExecuteService {
    SfcVo getSfcInfoBySfcIn(String sfc) throws CommonException;
    void enterStation(SfcDto sfcDto) throws CommonException;
    void enterStationByFirstOperation(SfcDto sfcDto) throws CommonException;
    SfcVo getSfcInfoBySfcOut(OutStationDto outStationDto) throws CommonException;
    void outStation(OutStationDto outStationDto) throws CommonException;
    void enterStationNew(SfcDto sfcDto) throws CommonException;
    int outStationNew(OutStationDto outStationDto) throws CommonException;
    SfcVo getSfcInfoBySfcOutNew(OutStationDto outStationDto) throws CommonException;
}
