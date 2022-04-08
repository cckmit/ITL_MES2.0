package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.OperationOrderDTO;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.OperationOrder;
import com.itl.mes.core.api.service.OperationOrderService;
import com.itl.mes.core.provider.mapper.OperationMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.camunda.commons.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operationOrder")
@Api(tags = " 工序工单" )
public class OperationOrderController {
    @Autowired
    private OperationOrderService operationOrderService;

    @Autowired
    private OperationMapper operationMapper;

    @ApiOperation(value="查询所有需要派工的工序工单信息")
    @PostMapping("/needDispatchOperationOrder")
    public ResponseData<IPage<OperationOrder>> needDispatchOperationOrder(@RequestBody OperationOrderDTO operationOrderDTO){
        return ResponseData.success(operationOrderService.selectOperationOrder(operationOrderDTO));
    }
    /**
     * 查询所有需要派工的工序
     * &
     * @return
     */
    @PostMapping("/queryCanDispatchOperation")
    @ApiOperation(value="查询所有需要派工的工序")
    public ResponseData<IPage<Operation>> queryCanDispatchOperation(@RequestBody OperationOrderDTO operationOrderDTO){
        return ResponseData.success(operationMapper.queryCanDispatchOperation(operationOrderDTO.getPage(),operationOrderDTO));
    }

    /**
     * 查询所有下达的工序工单
     * &
     */
    @ApiOperation(value="查询所有下达的工序工单")
    @PostMapping("/queryOperationOrder")
    public ResponseData<IPage<OperationOrder>> queryOperationOrder(@RequestBody OperationOrderDTO operationOrderDTO){
        QueryWrapper<OperationOrder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(operationOrderDTO.getOperationOrder())){
            queryWrapper.eq("operation_order",operationOrderDTO.getOperationOrder());
        }
        return ResponseData.success(operationOrderService.page(operationOrderDTO.getPage(),queryWrapper));
    }

}
