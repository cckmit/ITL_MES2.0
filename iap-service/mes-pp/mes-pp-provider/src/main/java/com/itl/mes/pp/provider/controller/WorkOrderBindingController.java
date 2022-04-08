package com.itl.mes.pp.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.pp.provider.service.impl.WorkOrderBindingServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api("排程工单")
@RestController
@RequestMapping("/schedule/work/order")
public class WorkOrderBindingController {

    @Autowired
    private WorkOrderBindingServiceImpl workOrderBindingService;

    @PostMapping("/add")
    @ApiOperation(value="工单绑定")
    @ApiImplicitParam(name="binding",value="工单",dataType="string", paramType = "query")
    public ResponseData add(@RequestBody List<Map<String, Object>> params){
        workOrderBindingService.add(params);
        return ResponseData.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value="工单解绑")
    @ApiImplicitParam(name="delete",value="工单解绑",dataType="string", paramType = "query")
    public ResponseData delete(@RequestBody List<String> ids){
        workOrderBindingService.delete(ids);
        return ResponseData.success();
    }

    @PostMapping("/assignedBand")
    @ApiOperation(value="机台指配")
    @ApiImplicitParam(name="assignedBand",value="机台指配",dataType="string", paramType = "query")
    public ResponseData assignedBand(@RequestBody List<Map<String, Object>> params){
        return ResponseData.success(workOrderBindingService.assignedBand(params));
    }

    @PostMapping("/assignedLine")
    @ApiOperation(value="产线指配")
    @ApiImplicitParam(name="assignedLine",value="产线指配",dataType="string", paramType = "query")
    public ResponseData assignedLine(@RequestBody List<Map<String, Object>> params, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){
        return ResponseData.success(workOrderBindingService.assignedLine(params, page, pageSize));
    }

    @PostMapping("/save")
    @ApiOperation(value="保存")
    @ApiImplicitParam(name="save",value="保存",dataType="string", paramType = "query")
    public ResponseData save(@RequestBody List<Map<String, Object>> params){
        workOrderBindingService.save(params);
        return ResponseData.success();
    }
}
