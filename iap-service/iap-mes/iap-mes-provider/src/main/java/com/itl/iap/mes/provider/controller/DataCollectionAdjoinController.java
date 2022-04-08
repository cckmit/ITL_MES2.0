package com.itl.iap.mes.provider.controller;


import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.DataCollectionAdjoin;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import com.itl.iap.mes.provider.service.impl.DataCollectionAdjoinServiceImpl;
import com.itl.iap.mes.provider.service.impl.DataCollectionItemServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/m/data/collection/adjoin")
public class DataCollectionAdjoinController extends BaseController {

    @Autowired
    private DataCollectionAdjoinServiceImpl dataCollectionAdjoinService;

    @ApiOperation(value = "list", notes = "根据数据收集id查询所有附属点", httpMethod = "GET")
    @PostMapping(value = "/list")
    public ResponseData queryPage(@RequestParam String dataCollectionId, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(dataCollectionAdjoinService.findList(dataCollectionId,page,pageSize));
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(dataCollectionAdjoinService.findById(id));
    }

    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody DataCollectionAdjoin dataCollectionAdjoin) {
        dataCollectionAdjoinService.save(dataCollectionAdjoin);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        dataCollectionAdjoinService.delete(ids);
        return ResponseData.success(true);
    }

}