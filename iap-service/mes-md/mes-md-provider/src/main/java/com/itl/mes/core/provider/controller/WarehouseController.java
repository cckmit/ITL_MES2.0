package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.Warehouse;
import com.itl.mes.core.api.service.WarehouseService;
import com.itl.mes.core.api.vo.WarehouseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author space
 * @since 2019-06-18
 */
@RestController
@RequestMapping("/warehouses")
@Api(tags = " 线边仓信息表" )
public class WarehouseController {
    private final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public WarehouseService warehouseService;

    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<Warehouse> getWarehouseById(@PathVariable String id) {
        return ResponseData.success(warehouseService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value="保存线边仓信息")
    public ResponseData<WarehouseVo> saveWarehouse(@RequestBody WarehouseVo warehouseVo) throws CommonException {
        warehouseService.save(warehouseVo);
        warehouseVo =  warehouseService.getWarehouseVo(warehouseVo.getWarehouse());
        return ResponseData.success(warehouseVo);
    }

    @GetMapping("/query")
    @ApiOperation(value="查询线边仓信息")
    public ResponseData<WarehouseVo> getWarehouseVo(String warehouse) throws CommonException {
        WarehouseVo warehouseVo = warehouseService.getWarehouseVo(warehouse);
        return ResponseData.success(warehouseVo);
    }

    @GetMapping("/delete")
    @ApiOperation(value="删除线边仓信息")
    public ResponseData<String> delete(String warehouse,String modifyDate) throws CommonException {
        warehouseService.delete(warehouse, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return  ResponseData.success("success");
    }
    
}