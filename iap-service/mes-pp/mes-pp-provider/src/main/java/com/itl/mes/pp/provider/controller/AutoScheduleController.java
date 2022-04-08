package com.itl.mes.pp.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.entity.Strategy;
import com.itl.mes.pp.provider.config.Constant;
import com.itl.mes.pp.provider.service.impl.AutoScheduleServiceImpl;
import com.itl.mes.pp.provider.service.impl.WorkOrderBindingServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("自动排程")
@RestController
@RequestMapping("/auto/schedule")
public class AutoScheduleController {

    @Autowired
    private AutoScheduleServiceImpl autoScheduleService;

    @PostMapping("/addStrategy")
    @ApiOperation(value="新增策略")
    @ApiImplicitParam(name="addStrategy",value="新增策略",dataType="string", paramType = "query")
    public ResponseData addStrategy(@RequestBody List<Strategy> strategyList){
        autoScheduleService.addStrategy(strategyList);
        return ResponseData.success();
    }


    @PostMapping("/queryStrategy")
    @ApiOperation(value="查询策略")
    @ApiImplicitParam(name="queryStrategy",value="查询策略",dataType="string", paramType = "query")
    public ResponseData queryStrategy(@RequestBody Strategy strategy){
        return ResponseData.success(autoScheduleService.queryStrategy(strategy));
    }

    @PostMapping("/queryPlanSchedule")
    @ApiOperation(value="计划排程列表")
    @ApiImplicitParam(name="queryPlanSchedule",value="计划排程列表",dataType="string", paramType = "query")
    public ResponseData queryPlanSchedule(@RequestBody Map<String,Object> params, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){
        return ResponseData.success(autoScheduleService.queryPlanSchedule(params, page, pageSize, null));
    }

    @PostMapping("/queryTaskLock")
    @ApiOperation(value="查询可锁定的任务")
    @ApiImplicitParam(name="queryTaskLock",value="查询可锁定的任务",dataType="string", paramType = "query")
    public ResponseData queryTaskLock(@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize){
        return ResponseData.success(autoScheduleService.queryTaskLock(page, pageSize));
    }

    @PostMapping("/taskLock")
    @ApiOperation(value="任务锁定")
    @ApiImplicitParam(name="taskLock",value="任务锁定",dataType="string", paramType = "query")
    public ResponseData taskLock(@RequestBody List<Map<String, Object>> paramsList){
        autoScheduleService.taskLock(paramsList);
        return ResponseData.success();
    }

    @PostMapping("/noTaskLock")
    @ApiOperation(value="任务取消锁定")
    @ApiImplicitParam(name="noTaskLock",value="任务取消锁定",dataType="string", paramType = "query")
    public ResponseData noTaskLock(@RequestBody List<Map<String, Object>> paramsList){
        autoScheduleService.noTaskLock(paramsList);
        return ResponseData.success();
    }

    @PostMapping("/moveOrder")
    @ApiOperation(value="移动工单接口")
    @ApiImplicitParam(name="moveOrder",value="移动工单接口",dataType="string", paramType = "query")
    public ResponseData moveOrder(@RequestParam("shopOrder") String shopOrder, @RequestParam("type") Integer type, @RequestParam("productLineBo") String productLineBo){
        autoScheduleService.moveOrder(shopOrder, type, productLineBo);
        return ResponseData.success();
    }

    @PostMapping("/getExceptionByLine")
    @ApiOperation(value="根据产线获取异常信息")
    @ApiImplicitParam(name="getExceptionByLine",value="getExceptionByLine",dataType="string", paramType = "query")
    public ResponseData getExceptionByLine(@RequestBody Map<String, Object> params){
        return ResponseData.success(autoScheduleService.getExceptionByLine(params));
    }


    @PostMapping("/autoSchedule")
    @ApiOperation(value="自动排程接口")
    @ApiImplicitParam(name="autoSchedule",value="自动排程接口",dataType="string", paramType = "query")
    public ResponseData autoSchedule(@RequestBody Map<String,Object> params){
        autoScheduleService.autoSchedule(params, Constant.IS_MOVE,null);
        return ResponseData.success();
    }

    @PostMapping("/cancelSchedule")
    @ApiOperation(value="撤销调整")
    @ApiImplicitParam(name="cancelSchedule",value="撤销调整",dataType="string", paramType = "query")
    public ResponseData cancelSchedule(@RequestBody Map<String,Object> params){
        autoScheduleService.cancelSchedule(params);
        return ResponseData.success();
    }

    @PostMapping("/callOffSchedule")
    @ApiOperation(value="排程取消")
    @ApiImplicitParam(name="callOffSchedule",value="排程取消",dataType="string", paramType = "query")
    public ResponseData callOffSchedule(@RequestBody List<String> shopOrderList){
        autoScheduleService.callOffSchedule(shopOrderList);
        return ResponseData.success();
    }

    @PostMapping("/issuedOrder")
    @ApiOperation(value="工单下达")
    @ApiImplicitParam(name="issuedOrder",value="工单下达",dataType="string", paramType = "query")
    public ResponseData issuedOrder(@RequestBody List<String> shopOrderList){
        autoScheduleService.issuedOrder(shopOrderList);
        return ResponseData.success();
    }

    @PostMapping("/noIssuedOrder")
    @ApiOperation(value="工单取消下达")
    @ApiImplicitParam(name="noIssuedOrder",value="工单下达",dataType="string", paramType = "query")
    public ResponseData noIssuedOrder(@RequestBody List<String> shopOrderList){
        autoScheduleService.noIssuedOrder(shopOrderList);
        return ResponseData.success();
    }
}
