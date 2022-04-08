package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupQueryDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolGroupEntity;
import com.itl.iap.mes.provider.service.impl.ToolGroupServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/6 15:52
 */
@RestController
@RequestMapping("/m/toolGroup")
public class ToolGroupController {

    @Autowired
    ToolGroupServiceImpl toolGroupService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData findList(@RequestBody ToolGroupQueryDTO toolGroupQueryDTO) {
        return ResponseData.success(toolGroupService.findList(toolGroupQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody ToolGroupEntity toolGroupEntity) {
        toolGroupService.save(toolGroupEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        toolGroupService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(toolGroupService.findById(id));
    }




}
