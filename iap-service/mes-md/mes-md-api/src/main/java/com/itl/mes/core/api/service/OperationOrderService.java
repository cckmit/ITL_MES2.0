package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.OperationOrderDTO;
import com.itl.mes.core.api.entity.OperationOrder;

public interface OperationOrderService extends IService<OperationOrder> {
    IPage<OperationOrder> selectOperationOrder(OperationOrderDTO operationOrderDTO);
}
