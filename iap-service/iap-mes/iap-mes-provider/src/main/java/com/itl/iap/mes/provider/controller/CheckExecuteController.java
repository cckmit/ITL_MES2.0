package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.CheckExecute;
import com.itl.iap.mes.provider.service.impl.CheckExecuteServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/m/repair/execute")
public class CheckExecuteController extends BaseController {

    @Autowired
    private CheckExecuteServiceImpl checkExecuteService;


    @ApiOperation(value = "list", notes = "查询list", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData queryPage(@RequestBody CheckExecute checkExecute, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(checkExecuteService.findList(checkExecute,page,pageSize));
    }

    @ApiOperation(value = "getById", notes = "查询单条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(checkExecuteService.findById(id));
    }

    @ApiOperation(value = "save", notes = "保存", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody CheckExecute checkExecute) {
        checkExecuteService.save(checkExecute);
        return ResponseData.success(true);
    }
}