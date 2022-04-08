package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.OkStemDispatchDTO;
import com.itl.mes.core.api.dto.StemDispatchDTO;
import com.itl.mes.core.api.dto.StemReportWorkDto;
import com.itl.mes.core.api.entity.OperationOrder;
import com.itl.mes.core.api.entity.StemDispatch;
import com.itl.mes.core.api.vo.StemDispatchListVo;
import com.itl.mes.core.api.vo.StemDispatchVo;
import com.itl.mes.core.api.vo.StepNcVo;

import java.io.IOException;
import java.util.List;

public interface StemDispatchService extends IService<StemDispatch> {
    IPage<OperationOrder> queryNeedDispatchOrders(StemDispatchDTO stemDispatchDTO);
    List<StemDispatchVo> assignment(StemDispatchDTO stemDispatchDTO) throws CommonException;
    void okAssignment(OkStemDispatchDTO okStemDispatchDTO);
    void exportStemDispatch(StemDispatchDTO stemDispatchDTO) throws CommonException, IOException;
    List<StemDispatchListVo> selectStemReportWorkList(List<StemDispatchDTO> stemDispatchDTO);
    void okStemReportWork(List<StemReportWorkDto> stemReportWorkDtoList) throws CommonException;
    StepNcVo getAllInfoByShopOrderBo(String shopOrderBo) throws CommonException;
    void cancelDispatchOrder(List<String> dispatchOrders) throws CommonException;
}
