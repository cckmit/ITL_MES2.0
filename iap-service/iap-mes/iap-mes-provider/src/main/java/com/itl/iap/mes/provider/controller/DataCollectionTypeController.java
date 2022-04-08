package com.itl.iap.mes.provider.controller;


import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.DataCollectionType;
import com.itl.iap.mes.provider.service.impl.DataCollectionTypeServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/m/data/collection/type")
public class DataCollectionTypeController extends BaseController {

    @Autowired
    private DataCollectionTypeServiceImpl dataCollectionService;



    @ApiOperation(value = "list", notes = "根据分组id查询字典", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData list(@RequestBody DataCollectionType dataCollectionType, @RequestParam Integer page, @RequestParam Integer pageSize) {
       return ResponseData.success(dataCollectionService.findList(dataCollectionType,page,pageSize));
    }

    @ApiOperation(value = "listByState", notes = "根据分组id查询字典", httpMethod = "POST")
    @PostMapping(value = "/listByState")
    public ResponseData listByState(@RequestBody DataCollectionType dataCollectionType, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(dataCollectionService.findListByState(dataCollectionType,page,pageSize));
    }

    @ApiOperation(value = "queryForLov", notes = "lov查询", httpMethod = "POST")
    @PostMapping(value = "/queryForLov")
    public ResponseData queryForLov(@RequestBody Map<String, Object> params) {
        return ResponseData.success(dataCollectionService.queryForLov(params));
    }

    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody DataCollectionType dataCollection) {
        dataCollectionService.save(dataCollection);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        dataCollectionService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(dataCollectionService.findById(id));

    }

}
