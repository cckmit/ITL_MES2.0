package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.dto.OkReportWorkDto;
import com.itl.mes.core.api.dto.ReportWorkDto;
import com.itl.mes.core.api.dto.ReportWorkListDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.ReportWorkService;
import com.itl.mes.core.api.vo.ReportWorkVo;
import com.itl.mes.core.api.vo.StepScrapVo;
import com.itl.mes.core.provider.mapper.*;
import com.itl.mes.core.provider.service.impl.SfcServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/reportWork")
@Api(tags = "工步报工" )
public class ReportWorkController {

    private final Logger logger = LoggerFactory.getLogger(ReportWorkController.class);

    @Autowired
    private ReportWorkService reportWorkService;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private ReportWorkMapper reportWorkMapper;

    @Autowired
    private UserUtil userUtil;

    @PostMapping("/getCanReportWorkData")
    @ApiOperation(value="查询该工步某天可报工的数据")
    public ResponseData<IPage<ReportWorkVo>> getCanReportWorkData(@RequestBody ReportWorkDto reportWorkDto){
        return ResponseData.success(reportWorkService.getCanReportWorkData(reportWorkDto));
    }

    @ApiOperation(value="确认报工")
    @PostMapping("/okReportWork")
    public ResponseData okReportWork(@RequestBody List<OkReportWorkDto> okReportWorkDtoList) throws CommonException{
        for(OkReportWorkDto okReportWorkDto:okReportWorkDtoList){
            // 校验当前工序是否已配置
            /*if(!reportWorkService.isConfig(okReportWorkDto)){
                throw new CommonException("存在工步未配置,不能进行工步报工",30002);
            }*/
            ReportWork reportWork = new ReportWork();
            BeanUtils.copyProperties(okReportWorkDto,reportWork);
            reportWork.setBo(UUID.uuid32());
            reportWork.setCreateTime(new Date());
            reportWork.setQty(okReportWorkDto.getWorkQty());
            reportWork.setTime(new Date());
            try {
                reportWork.setUserBo(sfcServiceImpl.getUserInfo().getUserBo());
            } catch (CommonException e) {
                e.printStackTrace();
            }
            if (okReportWorkDto.getWorkQty().compareTo(BigDecimal.ZERO) != 0){
                reportWorkService.save(reportWork);
            }
        }
        return ResponseData.success();
    }

    @ApiOperation(value="报工查询列表")
    @PostMapping("/selectReportWorkInfo")
    public ResponseData<IPage<ReportWork>> selectReportWorkInfo(@RequestBody ReportWorkListDto reportWorkListDto) throws CommonException, ParseException {
        return ResponseData.success(reportWorkService.selectReportWorkInfo(reportWorkListDto));
    }

    @ApiOperation(value="修改报工信息")
    @PostMapping("/updateReportWorkInfo")
    public ResponseData updateReportWorkInfo(@RequestBody ReportWorkListDto reportWorkDto){

        if ("1".equals(reportWorkDto.getState())){
            ResponseData.error("该报工单不能修改！");
        }
        if (ObjectUtil.isNotEmpty(reportWorkDto.getQty()) || ObjectUtil.isNotEmpty(reportWorkDto.getDifQty())){
            ReportWork reportWork = new ReportWork();
            reportWork.setQty(reportWorkDto.getQty());
            reportWork.setDifQty(reportWorkDto.getDifQty());
            reportWork.setBo(reportWorkDto.getBo());
            reportWork.setUpdateDate(new Date());
            reportWork.setUpdateBy(userUtil.getUser().getUserName());
            reportWorkService.updateById(reportWork);
        }
        return ResponseData.success();
    }

    @PostMapping("/updateWorkState")
    @ApiOperation(value="修改报工状态")
    public ResponseData updateWorkState(@RequestBody List<ReportWorkListDto> reportWorkListDtos ){
        reportWorkListDtos.forEach(
                reportWorkListDto -> {
                    ReportWork reportWork = new ReportWork();
                    reportWork.setState(reportWorkListDto.getState());
                    reportWork.setBo(reportWorkListDto.getBo());
                    reportWorkService.updateById(reportWork);
                }
        );
        return ResponseData.success();
    }

    @GetMapping("/getStationByUser")
    @ApiOperation(value = "根据用户查找下面所有的工位，再通过工位知道对应工步")
    public ResponseData<List<WorkStation>> getStationByUser() throws CommonException {
        String userId = sfcServiceImpl.getUserInfo().getUserId();
        return ResponseData.success(reportWorkMapper.getStationByUser(userId));
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportReportWork(@RequestBody ReportWorkListDto reportWorkListDto) {
        try {
            reportWorkService.exportReportWork(reportWorkListDto);
        } catch (CommonException | ParseException e) {
            logger.error("exportReportWork -=- {}", ExceptionUtils.getFullStackTrace(e));
        }
    }
    @PostMapping("/selectReportWorkRecord")
    @ApiOperation(value = "查询当前登录人某天的报工数量")
    public ResponseData<IPage<ReportWork>> selectReportWorkRecord(@RequestBody ReportWorkDto reportWorkDto) throws CommonException {
        reportWorkDto.setUserBo(sfcServiceImpl.getUserInfo().getUserBo());
        if(StringUtils.isNotEmpty(reportWorkDto.getTime())) {
            String startTime = reportWorkDto.getTime() + " 00:00:00";
            String endTime = reportWorkDto.getTime() + " 23:59:59";
            reportWorkDto.setStartTime(startTime);
            reportWorkDto.setEndTime(endTime);
        }
        return ResponseData.success(reportWorkMapper.selectReportWorkRecord(reportWorkDto.getPage(),reportWorkDto));
    }

//    @GetMapping("/getScrapInfoBySfc")
//    @ApiOperation(value = "根据sfc获取工步报废相关信息")
//    public ResponseData<StepScrapVo> getScrapInfoBySfc(@RequestParam("sfc") String sfc) throws CommonException{
//        StepScrapVo stepScrapBySfc = reportWorkMapper.getStepScrapBySfc(sfc);
//        if (!stepScrapBySfc.getState().equals("排队中")){
//            throw new CommonException("排队中的sfc才能登记工步报废",504);
//        }
//        if (stepScrapBySfc.getMaxScrapQty().compareTo(BigDecimal.ZERO) == 0){
//            throw new CommonException("该sfc没有可报废数量",504);
//        }
//        //找到当前工序的上一个工序
//        String scrapOperationBo = productionExecuteServiceImpl.getPreviousOpByCurrentOp(stepScrapBySfc.getRouterBo(), stepScrapBySfc.getCurrentOperationBo());
//        Operation operation = operationMapper.selectById(scrapOperationBo);
//        //校验当前工序是否是这个工序
//        if (!scrapOperationBo.equals(sfcServiceImpl.getUserInfo().getOperationBo())){
//            throw new CommonException("该sfc要记录报废的工序为" + operation.getOperationName() + ",请切换工序",504);
//        }
//        stepScrapBySfc.setScrapOperation(operation.getOperation());
//        stepScrapBySfc.setScrapOperationName(operation.getOperationName());
//        stepScrapBySfc.setScrapOperationBo(operation.getBo());
//        stepScrapBySfc.setScrapStation(sfcServiceImpl.getUserInfo().getStation());
//        stepScrapBySfc.setScrapStationBo(sfcServiceImpl.getUserInfo().getStationBo());
//        stepScrapBySfc.setScrapStationName(sfcServiceImpl.getUserInfo().getStationName());
//        stepScrapBySfc.setUserBo(sfcServiceImpl.getUserInfo().getUserBo());
//        stepScrapBySfc.setUserName(sfcServiceImpl.getUserInfo().getUserName());
//        return ResponseData.success(stepScrapBySfc);
//    }

    @GetMapping("/getScrapInfoBySfc")
    @ApiOperation(value = "根据sfc获取工步报废相关信息")
    public ResponseData<StepScrapVo> getScrapInfoBySfc(@RequestParam("sfc") String sfc) throws CommonException{
        StepScrapVo stepScrapBySfc = reportWorkMapper.getStepScrapBySfc(sfc);
        if (ObjectUtil.isEmpty(stepScrapBySfc)){
            throw new CommonException("该sfc不存在",504);
        }
        UserInfo userInfo = sfcServiceImpl.getUserInfo();

        stepScrapBySfc.setScrapOperation(userInfo.getOperation());
        stepScrapBySfc.setScrapOperationName(userInfo.getOperationName());
        stepScrapBySfc.setScrapOperationBo(userInfo.getOperationBo());
        stepScrapBySfc.setScrapStation(userInfo.getStation());
        stepScrapBySfc.setScrapStationBo(userInfo.getStationBo());
        stepScrapBySfc.setScrapStationName(userInfo.getStationName());
        stepScrapBySfc.setUserBo(userInfo.getUserBo());
        stepScrapBySfc.setUserName(userInfo.getUserName());

        return ResponseData.success(stepScrapBySfc);
    }

    @PostMapping("/scrapRegister")
    @ApiOperation(value = "报废登记")
    public ResponseData scrapRegister(@RequestBody List<StepScrapVo> stepScrapVo) throws CommonException {
        reportWorkService.scrapRegister(stepScrapVo);
        return ResponseData.success();
    }
}
