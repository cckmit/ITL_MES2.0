package com.itl.iap.report.provider.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.DateUtil;
import com.itl.iap.report.api.dto.*;
import com.itl.iap.report.api.entity.SfcWipLog;
import com.itl.iap.report.api.service.SfcWipLogReportService;
import com.itl.iap.report.api.vo.*;
import com.itl.iap.report.provider.mapper.SfcWipLogReportMapper;
import com.itl.iap.report.provider.service.impl.SfcWipLogReportServiceImpl;
import com.itl.mes.core.api.vo.SfcDeviceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sfcWipLogReport")
@Api(tags = "生产过程日志报表" )
public class SfcWipLogReportController {

    @Autowired
    public SfcWipLogReportService sfcWipLogReportService;

    @Autowired
    private SfcWipLogReportMapper sfcWipLogReportMapper;

    @PostMapping("/list")
    @ApiOperation("分页查询生产过程日志报表信息")
    public ResponseData<IPage<SfcWipLog>> list(@RequestBody SfcWipLogReportDto sfcWipLogReportDto){
        return ResponseData.success(sfcWipLogReportService.queryList(sfcWipLogReportDto));
    }

    @PostMapping("/pullIn")
    @ApiOperation("进站报表")
    public ResponseData<IPage<SfcDeviceVO>> list(@RequestBody SfcDeviceVO sfcDeviceVO){
        return ResponseData.success(sfcWipLogReportService.queryPullIn(sfcDeviceVO));
    }

    @PostMapping("/productInfo")
    @ApiOperation("产线产能报表")
    public ResponseData<IPage<ProductVo>> queryProcutInfo(@RequestBody ProductDto productDto){
        return ResponseData.success(sfcWipLogReportService.queryProductInfo(productDto));
    }

    @PostMapping("/productInfoByOperationBo")
    @ApiOperation("根据工序查询产线产能报表")
    public ResponseData<IPage<ProductOperationVo>> queryProcutInfoByOperationBo(@RequestBody ProductDto productDto){
        return ResponseData.success(sfcWipLogReportService.selectProductInfoByOperationBo(productDto));
    }

    @PostMapping("/productShopOrderInfo")
    @ApiOperation("根据工序和物料查询工单产线产能报表")
    public ResponseData<IPage<ProductShopOrderVo>> productShopOrderInfo(@RequestBody ProductDto productDto){
        return ResponseData.success(sfcWipLogReportService.selectProductShopOrderInfo(productDto));
    }

    @PostMapping("/userProduct")
    @ApiOperation("员工产能表")
    public ResponseData<UserProductVo> userProduct(@RequestBody UserProductDto userProductDto){
        return ResponseData.success(sfcWipLogReportService.selectUserProduct(userProductDto));
    }

    @ApiOperation("工序产能报表")
    @PostMapping("/listOperationCapacity")
    public ResponseData<IPage<OperationCapacityVo>> listOperationCapacity(@RequestBody OperationCapacityDto operationCapacityDto){
        return ResponseData.success(sfcWipLogReportService.listOperationCapacity(operationCapacityDto));
    }

    @ApiOperation("工序产能明细")
    @PostMapping("/getOperationCapacityDetailed")
    public ResponseData<IPage<OperationCapacityDetailedVo>> getOperationCapacityDetailed(@RequestBody OperationCapacityDto operationCapacityDto){
        return ResponseData.success(sfcWipLogReportMapper.getOperationCapacityDetailed(operationCapacityDto.getPage(),operationCapacityDto));
    }

    @ApiOperation("员工出勤报表")
    @PostMapping("/listStaffAttendance")
    public ResponseData<IPage<StaffAttendanceVo>> listStaffAttendance(@RequestBody StaffAttendanceDto staffAttendanceDto) throws ParseException {
        return ResponseData.success(sfcWipLogReportService.listStaffAttendance(staffAttendanceDto));
    }

    @ApiOperation("员工日产能报表")
    @PostMapping("/listStaffDailyEnergy")
    public ResponseData<IPage<StaffDailyEnergyVo>> listStaffDailyEnergy(@RequestBody StaffDailyEnergyVo staffDailyEnergyVo){
        SfcWipLogReportServiceImpl.encapsulationDate(staffDailyEnergyVo);
        return ResponseData.success(sfcWipLogReportMapper.getStaffDailyEnergyPageList(staffDailyEnergyVo.getPage(),staffDailyEnergyVo));
    }

    @ApiOperation("员工日产能加工数汇总")
    @PostMapping("/sumStaffDailyEnergy")
    public ResponseData<String> sumStaffDailyEnergy(@RequestBody StaffDailyEnergyVo staffDailyEnergyVo){
        SfcWipLogReportServiceImpl.encapsulationDate(staffDailyEnergyVo);
        return ResponseData.success(sfcWipLogReportMapper.getSumStaffDailyEnergy(staffDailyEnergyVo));
    }

    @ApiOperation("员工日产能加工明细")
    @PostMapping("/staffDailyEnergyMachiningDetails")
    public ResponseData<List<StaffDailyEnergyVo>> staffDailyEnergyMachiningDetails(@RequestBody StaffDailyEnergyVo staffDailyEnergyVo){
        SfcWipLogReportServiceImpl.encapsulationDate(staffDailyEnergyVo);
        return ResponseData.success(sfcWipLogReportMapper.staffDailyEnergyMachiningDetails(staffDailyEnergyVo));
    }

    @ApiOperation("员工日产能安灯明细")
    @PostMapping("/staffDailyEnergyAndonDetails")
    public ResponseData<List<StaffDailyEnergyAndonDetailsVo>> staffDailyEnergyAndonDetails(@RequestBody StaffDailyEnergyAndonDetailsVo staffDailyEnergyAndonDetailsVo){
        List<StaffDailyEnergyAndonDetailsVo> staffDailyEnergyAndonDetailsVos = sfcWipLogReportMapper.staffDailyEnergyAndonDetails(staffDailyEnergyAndonDetailsVo);
        for (StaffDailyEnergyAndonDetailsVo dailyEnergyAndonDetailsVo : staffDailyEnergyAndonDetailsVos) {
            if (StringUtils.isNotBlank(dailyEnergyAndonDetailsVo.getProcessingTime())){
                dailyEnergyAndonDetailsVo.setProcessingTime(DateUtil.formatDateTime(Long.valueOf(dailyEnergyAndonDetailsVo.getProcessingTime())));
            }
        }
        return ResponseData.success(staffDailyEnergyAndonDetailsVos);
    }

    @ApiOperation("计划达成报表")
    @PostMapping("/planReachedReport")
    public ResponseData<IPage<PlanReachedVo>> planReachedReport(@RequestBody PlanReachedDto planReachedDto){
        return ResponseData.success(sfcWipLogReportService.listPlanReached(planReachedDto));
    }

    @ApiOperation("计划达成报表工单明细")
    @PostMapping("/planReachedOrderDetails")
    public ResponseData<List<PlanReachedOrderDetailsVo>> planReachedOrderDetails(@RequestBody PlanReachedDto planReachedDto){
        return ResponseData.success(sfcWipLogReportService.planReachedOrderDetails(planReachedDto));
    }

    @ApiOperation("计划达成报表在制明细")
    @PostMapping("/planReachedMakingDetails")
    public ResponseData<List<PlanReachedMakingDetailsVo>> planReachedMakingDetails(@RequestBody PlanReachedDto planReachedDto){
        SfcWipLogReportServiceImpl.encapsulationDate(planReachedDto);
        return ResponseData.success(sfcWipLogReportMapper.planReachedMakingDetails(planReachedDto));
    }

    @ApiOperation("员工产能报表-改制")
    @PostMapping("/updateUserReform")
    public ResponseData updateUserReform(@RequestBody List<UserProduct> userProductList){
        if(CollectionUtil.isNotEmpty(userProductList)){
            userProductList.forEach(userProduct->{
                sfcWipLogReportMapper.updateUserReform(userProduct.getBo());
            });
        }
        return  ResponseData.success();
    }
}
