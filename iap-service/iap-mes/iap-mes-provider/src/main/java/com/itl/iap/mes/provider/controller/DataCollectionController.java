package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.DataCollection;
import com.itl.iap.mes.provider.service.impl.DataCollectionServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/m/data/collection")
public class DataCollectionController extends BaseController {

    @Autowired
    private DataCollectionServiceImpl dataCollectionService;



    @ApiOperation(value = "list", notes = "数据收集组列表", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData getUserRoleIds(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return ResponseData.success(dataCollectionService.findList(type,name,page,pageSize));

    }

    @ApiOperation(value = "listByState", notes = "数据收集组列表", httpMethod = "POST")
    @PostMapping(value = "/listByState")
    public ResponseData getUserRoleIdsByState(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return ResponseData.success(dataCollectionService.findListByState(type,name,page,pageSize));

    }

    @ApiOperation(value = "queryForLov", notes = "lov查询", httpMethod = "POST")
    @PostMapping(value = "/queryForLov")
    public ResponseData queryForLov(@RequestBody Map<String, Object> params) {
        return ResponseData.success(dataCollectionService.queryForLov(params));
    }

    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody DataCollection dataCollection) {
        dataCollectionService.save(dataCollection);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(dataCollectionService.findById(id));
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        dataCollectionService.delete(ids);
        return ResponseData.success(true);
    }
}
