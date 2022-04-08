package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.OutSourceDto;
import com.itl.mes.core.api.entity.OutSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OutSourceService extends IService<OutSource> {
    void saveOrUpdateOutSource(OutSource outSource);

    void deleteByBo(String bo);

    IPage<OutSource> queryPages(OutSourceDto outSourceDto);

    OutSource queryByBo(String bo);

    String createCode();

    List<String> selectExeceptionCode(String exceptionCode) throws CommonException;

    Map<String,String> selectInfoByCode(String exceptionCode) throws CommonException;

    Map<String,String> selectInfoByDevice(String device) throws CommonException;

}
