package com.itl.mes.andon.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.andon.api.dto.AndonExceptionDTO;
import com.itl.mes.andon.api.entity.AndonException;
import com.itl.mes.andon.api.vo.AndonExceptionVo;

import java.util.List;
import java.util.Map;

public interface AndonExceptionService extends IService<AndonException> {
    AndonException getAndonExceptionById(String id);

    IPage<AndonException> getAndonException(AndonExceptionDTO andonExceptionDTO);

    AndonException saveInUpdate(AndonExceptionVo andonExceptionVO) throws CommonException;

    int delete(List<Integer> Ids);

    void signin(AndonExceptionVo exceptionVO);

    void relieve(AndonExceptionVo exceptionVO);

    String userVerify(Map<String, Object> params) throws CommonException;

    AndonException saveOrUpdateRepair(AndonExceptionVo andonExceptionVo);

    IPage<AndonException> repairList(AndonExceptionDTO andonExceptionDTO);

    IPage<AndonException> myRepairTask(AndonExceptionDTO andonExceptionDTO);

    void getRepairTask(AndonExceptionVo exceptionVO);
}
