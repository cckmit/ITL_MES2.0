package com.itl.iap.mes.provider.controller;


import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import com.itl.iap.mes.provider.service.impl.UpkeepPlanServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/m/repair/upkeep")
public class UpkeepPlanController extends BaseController {

    @Autowired
    private UpkeepPlanServiceImpl upkeepPlanService;



    @ApiOperation(value = "list", notes = "根据分组id查询字典", httpMethod = "GET")
    @PostMapping(value = "/list")
    public ResponseData queryPage(@RequestBody UpkeepPlan upkeepPlan, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(upkeepPlanService.findList(upkeepPlan,page,pageSize));
    }
    @ApiOperation(value = "listByState", notes = "根据分组id查询字典", httpMethod = "GET")
    @PostMapping(value = "/listByState")
    public ResponseData queryPageByState(@RequestBody UpkeepPlan upkeepPlan, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(upkeepPlanService.findListByState(upkeepPlan,page,pageSize));
    }

    //type 0 保存 1 保存加执行
    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody UpkeepPlan upkeepPlan) {
        Integer type=1;
        upkeepPlanService.save(upkeepPlan,type);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查询单条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(upkeepPlanService.findById(id));
    }


    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        upkeepPlanService.delete(ids);
        return ResponseData.success(true);
    }

}