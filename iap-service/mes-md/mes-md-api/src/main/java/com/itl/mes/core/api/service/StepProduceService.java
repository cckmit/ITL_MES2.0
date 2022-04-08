package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.StepProduceDTO;
import com.itl.mes.core.api.entity.StemOperationConfig;
import com.itl.mes.core.api.entity.StepProduce;
import com.itl.mes.core.api.vo.StepProduceVo;

import java.util.List;

public interface StepProduceService extends IService<StepProduce> {
    IPage<StepProduceVo> getCanInputStationOpOrder(StepProduceDTO stepProduceDTO);
    IPage<StepProduceVo> getCanOutputStationOpOrder(StepProduceDTO stepProduceDTO);
    String stepOutStation(StepProduceDTO stepProduceDTOS) throws CommonException;
    StemOperationConfig selectCurrentOpConfig();
}
