package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.OperationOrderDTO;
import com.itl.mes.core.api.entity.OperationOrder;
import org.apache.ibatis.annotations.Param;

public interface OperationOrderMapper extends BaseMapper<OperationOrder> {

    IPage<OperationOrder> selectOperationOrder(Page page, @Param("operationOrderDTO") OperationOrderDTO operationOrderDTO);
}
