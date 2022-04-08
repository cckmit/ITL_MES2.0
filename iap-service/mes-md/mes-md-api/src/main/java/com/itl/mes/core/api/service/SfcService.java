package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.*;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.vo.ReworkVo;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface SfcService extends IService<Sfc> {
    IPage<Sfc> selectSfcPage(SfcDto sfcDto) throws CommonException;

    List<Sfc> selectSfc(String operationOrder) throws CommonException;

    Sfc selectBySfc(String sfc) throws CommonException;

    Sfc sfcSplit(SfcSplitDto sfcSplitDto) throws CommonException;

    void stopOperate(OutStationDto outStationDto) throws CommonException;

    List<ReworkVo> selectReworkList(String sfc) throws CommonException;

    void okRework(List<ReworkDto> reworkDto) throws CommonException;

    void okReworkNew(List<ReworkDto> reworkDto) throws CommonException;

    List<Map<String, Object>> wipData(Map<String,Object> params);

    void exportWipData(Map<String, Object> params, HttpServletResponse response) throws CommonException, IOException;

    List<Operation> getOperationByRouterBo(String routerBo,String sfc) throws CommonException;

    void skipStation(SkipStationDTO skipStationDTO) throws CommonException;

    Sfc inSfcSplit(SfcSplitDto sfcSplitDto) throws CommonException;

    Sfc getInSplitInfo(String sfc) throws CommonException;
}
