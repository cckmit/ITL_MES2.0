package com.itl.iap.mes.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.DeviceCheckDto;
import com.itl.iap.mes.api.entity.CheckPlan;
import com.itl.iap.mes.api.entity.DeviceCheck;
import com.itl.iap.mes.api.vo.CheckPlanVo;
import com.itl.iap.mes.api.vo.DeviceCheckVo;
import com.itl.iap.mes.provider.service.impl.CheckPlanServiceImpl;
import com.itl.iap.notice.client.NoticeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/m/repair/check")
public class CheckPlanController extends BaseController {

    @Autowired
    private CheckPlanServiceImpl checkPlanService;

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "list", notes = "根据分组id查询字典", httpMethod = "Post")
    @PostMapping(value = "/list")
    public ResponseData queryPage(@RequestBody CheckPlan checkPlan, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(checkPlanService.findList(checkPlan,page,pageSize));
    }
    @ApiOperation(value = "listByState", notes = "根据分组id查询字典ByState", httpMethod = "Post")
    @PostMapping(value = "/listByState")
    public ResponseData queryPageByState(@RequestBody CheckPlan checkPlan, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(checkPlanService.findListByState(checkPlan,page,pageSize));
    }

    //type 0 保存 1 保存加执行
    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody CheckPlan checkPlan) {
        Integer type=1;
        checkPlanService.save(checkPlan,type);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查询单条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(checkPlanService.findById(id));
    }


    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        checkPlanService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "录入",  httpMethod = "GET")
    @GetMapping(value = "/inputInfo")
    public ResponseData<CheckPlanVo> inputInfo() {
        return ResponseData.success(checkPlanService.getInfoById());
    }

    @ApiOperation(value="保存点检单信息  state 0 点检中 1 已完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name="deviceCheck",value="设备点检信息",dataType="string"),
            @ApiImplicitParam(name="deviceCheckItems",value="设备点检明细信息",dataType="string"),
            @ApiImplicitParam(name="startDate",value="开始时间",dataType="string"),
            @ApiImplicitParam(name="endDate",value="结束时间",dataType="string")
    })
    @PostMapping(value = "saveCheckOrder")
    public ResponseData saveCheckOrder(@RequestBody DeviceCheckDto deviceCheckDto){
        checkPlanService.saveCheckOrder(deviceCheckDto);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "根据点检单编号查询点检单明细")
    @ApiImplicitParam(name="deviceCheckCode",value="设备点检单编号",dataType="string")
    @GetMapping(value = "queryById")
    public ResponseData<DeviceCheckVo> queryById(@RequestParam("deviceCheckCode") String deviceCheckCode){
        return ResponseData.success(checkPlanService.selectById(deviceCheckCode));
    }

    @ApiOperation(value="分页查询点检单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="startDate",value="开始时间",dataType="string"),
            @ApiImplicitParam(name="endDate",value="结束时间",dataType="string"),
            @ApiImplicitParam(name="deviceCheck",value="设备点检信息",dataType="string"),
            @ApiImplicitParam(name="deviceCheckItem",value="设备点检明细信息",dataType="string"),
    })
    @PostMapping (value = "queryDeviceCheck")
    public ResponseData<IPage<DeviceCheck>> queryDeviceCheck(@RequestBody DeviceCheckDto deviceCheckDto){
        IPage<DeviceCheck> deviceCheckIPage=checkPlanService.selectDeviceCheck(deviceCheckDto.getPage(), deviceCheckDto);
        return ResponseData.success(deviceCheckIPage);
    }

    @ApiOperation(value="根据设备点检单编号删除数据")
    @ApiImplicitParam(name="deviceCheckCodes",value="设备点检单编号",dataType="string")
    @DeleteMapping(value = "deleteById")
    public ResponseData deleteById(@RequestBody List<String> deviceCheckCodes){
        checkPlanService.deletById(deviceCheckCodes);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "录入:根据所选设备的设备类型查询点检项",  httpMethod = "GET")
    @ApiImplicitParam(name="deviceType",value="设备类型",dataType="string")
    @GetMapping(value = "/queryByDeviceType")
    public ResponseData<CheckPlanVo> queryByDeviceType(@RequestParam("deviceType") String  deviceType) throws CommonException {
        return ResponseData.success(checkPlanService.queryByDeviceType(deviceType));
    }

    @PostMapping (value = "/test")
    public ResponseData test(){
       noticeService.sendMessage(new HashMap<>());
       return  null;
    }

}