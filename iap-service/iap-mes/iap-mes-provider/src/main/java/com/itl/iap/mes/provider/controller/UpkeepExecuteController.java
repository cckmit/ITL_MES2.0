package com.itl.iap.mes.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.DeviceCheckDto;
import com.itl.iap.mes.api.dto.UpkeepTableDto;
import com.itl.iap.mes.api.entity.DeviceCheck;
import com.itl.iap.mes.api.entity.UpkeepExecute;
import com.itl.iap.mes.api.entity.UpkeepTable;
import com.itl.iap.mes.provider.service.impl.UpkeepExecuteServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/m/repair/upkeep/execute")
public class UpkeepExecuteController extends BaseController {

    @Autowired
    private UpkeepExecuteServiceImpl upkeepExecuteService;



    @ApiOperation(value = "list", notes = "查询list", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData queryPage(@RequestBody UpkeepExecute upkeepExecute, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(upkeepExecuteService.findList(upkeepExecute,page,pageSize));
    }

    /*@ApiOperation(value = "getById", notes = "查询单条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(upkeepExecuteService.findById(id));
    }*/

    @ApiOperation(value = "save", notes = "保存", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody UpkeepExecute upkeepExecute) {
        upkeepExecuteService.save(upkeepExecute);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "录入", notes = "查询单条", httpMethod = "GET")
    @GetMapping(value = "/getById")
    public ResponseData<UpkeepTable> getById() {
        return ResponseData.success(upkeepExecuteService.queryById());
    }

    @ApiOperation(value = "保存保养单信息【提交 1 已保养 保存 0 保养中】", notes = "保存", httpMethod = "PUT")
    @PutMapping(value = "/saveRepairTable")
    public ResponseData saveRepairTable(@RequestBody UpkeepTable upkeepTable) {
        upkeepExecuteService.saveRepairTable(upkeepTable);
        return ResponseData.success(true);
    }

    @ApiOperation(value="分页查询保养单信息")
    @PostMapping (value = "queryRepairTable")
    public ResponseData<IPage<UpkeepTable>> queryDeviceCheck(@RequestBody UpkeepTableDto upkeepTableDto){
        IPage<UpkeepTable> upkeepTableIPage=upkeepExecuteService.selectRepairTable(upkeepTableDto);
        return ResponseData.success(upkeepTableIPage);
    }

    @ApiOperation(value="根据保养单编号查询保养明细信息")
    @GetMapping(value = "getByRepairId/{repairId}")
    public ResponseData<UpkeepTable> getByRepairId(@PathVariable String repairId){
        return ResponseData.success(upkeepExecuteService.queryByRepairId(repairId));
    }
    @ApiOperation(value = "保养执行根据id删除", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) throws CommonException {
        upkeepExecuteService.delete(ids);
        return ResponseData.success(true);
    }
    @ApiOperation(value = "保养列表根据repairId删除", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteByRepairId")
    public ResponseData deleteByRepairId(@RequestBody List<String> repairIds) throws CommonException {
        upkeepExecuteService.deleteByRepairId(repairIds);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "录入:根据所选设备的设备类型和类型查询点检项",  httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="deviceType",value="设备类型",dataType="string"),
            @ApiImplicitParam(name="upkeepPlanName" ,value="保养类型名称(季度保养0 周保养 1  日保养 0)",dataType="string")
    })
    @GetMapping(value = "/queryByDeviceType")
    public ResponseData<UpkeepTable> queryByDeviceType(@RequestParam("deviceType") String  deviceType,@RequestParam("upkeepPlanName") String upkeepPlanName) throws CommonException {
        return ResponseData.success(upkeepExecuteService.queryByDeviceType(deviceType,upkeepPlanName));
    }


}