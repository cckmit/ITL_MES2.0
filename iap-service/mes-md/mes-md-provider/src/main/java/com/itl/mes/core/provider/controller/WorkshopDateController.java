package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.service.WorkshopDateService;
import com.itl.mes.core.api.vo.CalendarShiftVo;
import com.itl.mes.core.api.vo.WorkshopCalendarVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * @author space
 * @since 2019-06-26
 */
@RestController
@RequestMapping("/workshopDates")
@Api(tags = " 车间日历表" )
public class WorkshopDateController {
    private final Logger logger = LoggerFactory.getLogger(WorkshopDateController.class);

    @Autowired
    public WorkshopDateService workshopDateService;



    @PostMapping("/save")
    @ApiOperation(value = "保存车间日历")
    public ResponseData<WorkshopCalendarVo> save(@RequestBody WorkshopCalendarVo workshopCalendarVo) throws CommonException {
        workshopDateService.save(workshopCalendarVo);
        workshopCalendarVo = workshopDateService.getWorkshopCalendarVo(workshopCalendarVo.getWorkshop(),workshopCalendarVo.getStartPeriod(),workshopCalendarVo.getEndPeriod());
        return ResponseData.success(workshopCalendarVo);
    }



    @GetMapping("/query")
    @ApiOperation(value = "查询车间日历")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workShop", value = "生产车间",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "startPeriod", value = "开始日期",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "endPeriod", value = "结束日期",dataType = "string",paramType = "query")
    })
    public ResponseData<WorkshopCalendarVo> getWorkshopCalendarVo(String workShop, String startPeriod, String endPeriod) throws CommonException {
        WorkshopCalendarVo workshopCalendarVo = workshopDateService.getWorkshopCalendarVo(workShop,startPeriod,endPeriod);
        return ResponseData.success(workshopCalendarVo);
    }


    @GetMapping("/delete")
    @ApiOperation(value = "删除车间日历")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workShop", value = "生产车间",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "startPeriod", value = "开始日期",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "endPeriod", value = "结束日期",dataType = "string",paramType = "query")
    })
    public ResponseData<String> delete(String workShop,String startPeriod, String endPeriod) throws CommonException {
        workshopDateService.delete(workShop,startPeriod,endPeriod);
        return  ResponseData.success("success");
    }


    @GetMapping("/getShiftDetailData")
    @ApiOperation(value = "页面初始化获取班次信息")
    public ResponseData<List<CalendarShiftVo>> getShiftDetailData() throws CommonException {
        List<CalendarShiftVo> shiftDetailData = workshopDateService.getShiftDetailData();
        return ResponseData.success(shiftDetailData);
    }
    
}