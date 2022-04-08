package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.demand.DemandQueryDTO;

import com.itl.iap.mes.api.entity.demand.DemandEntity;

import com.itl.iap.mes.provider.service.impl.DemandServerImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/6 9:29
 */
@RestController
@RequestMapping("/m/demand")
public class DemandController {


    @Autowired
    DemandServerImpl demandServer;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData findList(@RequestBody DemandQueryDTO demandQueryDTO) {
        return ResponseData.success(demandServer.findList(demandQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody DemandEntity demandEntity) {
        demandServer.save(demandEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        demandServer.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(demandServer.findById(id));
    }


}
