package com.itl.mes.core.provider.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.QualityPlanHandleBO;
import com.itl.mes.core.api.dto.OkReportWorkDto;
import com.itl.mes.core.api.dto.ReportWorkDto;
import com.itl.mes.core.api.dto.ReportWorkListDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.ReportWorkService;
import com.itl.mes.core.api.service.RouterProcessService;
import com.itl.mes.core.api.service.WorkStationService;
import com.itl.mes.core.api.vo.ReportWorkVo;
import com.itl.mes.core.api.vo.StepScrapVo;
import com.itl.mes.core.provider.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportWorkServiceImpl extends ServiceImpl<ReportWorkMapper, ReportWork> implements ReportWorkService {

    @Autowired
    private WorkStationService workStationService;
    @Autowired
    private ReportWorkMapper reportWorkMapper;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private RouterMapper routerMapper;
    @Autowired
    private RouteStationMapper routeStationMapper;
    @Autowired
    private SfcRepairMapper sfcRepairMapper;
    @Autowired
    private SfcMapper sfcMapper;
    @Autowired
    private RouterProcessService routerProcessService;
    @Autowired
    private SfcWiplogMapper sfcWiplogMapper;
    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Override
    public IPage<ReportWorkVo> getCanReportWorkData(ReportWorkDto reportWorkDto) {
        if (ObjectUtil.isEmpty(reportWorkDto.getPage())){
            reportWorkDto.setPage(new Page(0,10));
        }
        //通过传入的工步查询对应工序
        String workingProcess = workStationService.getById(reportWorkDto.getWorkStepCodeBo()).getWorkingProcess();
        reportWorkDto.setOperationBo(workingProcess);
        //拼接开始时间和结束时间，00:00:00 - 23:59:59 代表当天
        if (StringUtils.isNotBlank(reportWorkDto.getTime())){
            String startTime = reportWorkDto.getTime() + " 00:00:00";
            String endTime = reportWorkDto.getTime() + " 23:59:59";
            reportWorkDto.setStartTime(startTime);
            reportWorkDto.setEndTime(endTime);
        }
        IPage<ReportWorkVo> pageList = reportWorkMapper.selectCanReportWorkByStep(reportWorkDto.getPage(),reportWorkDto);
        List<ReportWorkVo> records = pageList.getRecords();
        //计算传入工步可报工数量并填充
        for (int i = 0; i < records.size(); i++) {
            reportWorkDto.setSfc(records.get(i).getSfc());
            List<ReportWork> canWorkQtyBySteps = reportWorkMapper.getCanWorkQtyByStep(reportWorkDto);
            BigDecimal canWorkQty = BigDecimal.ZERO;//当前工步报工数量，如果查不到数据，报工数量就为0
            if (CollectionUtil.isNotEmpty(canWorkQtyBySteps)){
                for (ReportWork canWorkQtyByStep : canWorkQtyBySteps) {
                    canWorkQty = canWorkQty.add(canWorkQtyByStep.getQty());
                }
            }
            //工步报工报废登记的报废数量
            String scarpQty = reportWorkMapper.getStepScrapQtyByWorkStep(reportWorkDto.getWorkStepCodeBo(), records.get(i).getSfc());
//            //可报工总数（没算上之前报的）
//            if (StringUtils.isBlank(scarpQty)){
//                scarpQty = "0";
//            }
            BigDecimal totalQty = records.get(i).getQty().subtract(new BigDecimal(scarpQty));
            //计算可报工数量
            BigDecimal canQty = totalQty.subtract(canWorkQty);
            if (canQty.compareTo(BigDecimal.ZERO) != 1){
                //说明报完了，则删除这条集合
                records.remove(i);
                i--;
                continue;
            }
            records.get(i).setCanReportWorkQty(canQty);
        }
        return pageList;
    }

    @Override
    public IPage<ReportWork> selectReportWorkInfo(ReportWorkListDto reportWorkListDto) throws CommonException, ParseException {
        String startTime = null;
        String endTime = null;
        if (StringUtils.isNotBlank(reportWorkListDto.getStartTime()) && StringUtils.isNotBlank(reportWorkListDto.getEndTime())){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (df.parse(reportWorkListDto.getStartTime()).getTime() > df.parse(reportWorkListDto.getEndTime()).getTime()){
                throw new CommonException("开始时间不能大于结束时间！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            startTime = reportWorkListDto.getStartTime() + " 00:00:00";
            endTime = reportWorkListDto.getEndTime() + " 23:59:59";
        }

        reportWorkListDto.setStartTime(startTime);
        reportWorkListDto.setEndTime(endTime);
        IPage<ReportWork> reportWorkIPage = reportWorkMapper.selectReportWorkInfo(reportWorkListDto.getPage(), reportWorkListDto);
        return reportWorkIPage;
    }

    @Override
    public void exportReportWork(ReportWorkListDto reportWorkListDto) throws CommonException, ParseException {
        QueryWrapper<ReportWork> queryWrapper = new QueryWrapper<ReportWork>();
        String startTime = null;
        String endTime = null;
        if (StringUtils.isNotBlank(reportWorkListDto.getStartTime()) && StringUtils.isNotBlank(reportWorkListDto.getEndTime())){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (df.parse(reportWorkListDto.getStartTime()).getTime() > df.parse(reportWorkListDto.getEndTime()).getTime()){
                throw new CommonException("开始时间不能大于结束时间！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            startTime = reportWorkListDto.getStartTime() + " 00:00:00";
            endTime = reportWorkListDto.getEndTime() + " 23:59:59";
        }
        reportWorkListDto.setStartTime(startTime);
        reportWorkListDto.setEndTime(endTime);
        List<ReportWork> reportWorkList = reportWorkMapper.selectReportWorkInfo( reportWorkListDto);
        for (ReportWork reportWork : reportWorkList) {
            if (StrUtil.isNotEmpty(reportWork.getWorkStepCodeBo())){
                String[] split = reportWork.getWorkStepCodeBo().split(",");
                String stepCode = split[4];
                reportWork.setWorkStepCode(stepCode);
            }
        }
        // 创建参数对象（用来设定excel得sheet得内容等信息）
        ExportParams ReportWorkExport = new ExportParams();
        // 设置sheet得名称
        ReportWorkExport.setSheetName("工步报工表");
        // 创建sheet1使用得map
        Map<String, Object> exportMap = new HashMap<>();
        // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
        exportMap.put("title", ReportWorkExport);
        // 模版导出对应得实体类型
        exportMap.put("entity", ReportWork.class);
        // sheet中要填充得数据
        exportMap.put("data", reportWorkList);

        // 将sheet1、sheet2、sheet3使用得map进行包装
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(exportMap);
        // 执行方法
        ExcelUtils.exportExcel(sheetsList,"质量控制计划数据表",response);
    }

    @Override
    public boolean isConfig(OkReportWorkDto okReportWorkDto) {
        // 通过sfc查询对应的工艺路线
        Router router= routerMapper.selectRouterBySfc(okReportWorkDto.getSfc());
       // 通过工步bo查询工步信息
        WorkStation workStation=workStationService.getById(okReportWorkDto.getWorkStepCodeBo());
        //查询当前工步是否已配置
        QueryWrapper<RouteStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_route",router.getRouter());
        queryWrapper.eq("route_ver",router.getVersion());
        queryWrapper.eq("work_step_code",workStation.getWorkStepCode());
        queryWrapper.eq("working_process",workStation.getWorkingProcess());
        RouteStation routeStation=routeStationMapper.selectOne(queryWrapper);
        // 工步配置表中不存在该工步信息，则未配置该工步
        if(routeStation==null) {
            return false;
        }
        String effective=routeStation.getEffective();
        if(effective.equals("0")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scrapRegister(List<StepScrapVo> stepScrapVo) throws CommonException {
        SfcRepair sfcRepair;
        SfcWiplog sfcWiplog;
        String sfc = stepScrapVo.get(0).getSfc();//当前sfc
        Date date = new Date();

        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfc));
        BigDecimal scrapQty = checkIsCanRecordScrap(sfcObj, stepScrapVo);//本次报废总数

        //把该sfc在当前工序的良品数相应减少
        List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("sfc", sfc).eq("operation_bo", sfcServiceImpl.getUserInfo().getOperationBo()));
        BigDecimal qty = scrapQty;
        for (SfcWiplog wiplog : sfcWiplogs) {
            if (wiplog.getDoneQty().compareTo(qty) != -1){
                wiplog.setDoneQty(wiplog.getDoneQty().subtract(qty));
                sfcWiplogMapper.updateById(wiplog);
                break;
            }else {
                wiplog.setDoneQty(BigDecimal.ZERO);
                qty = qty.subtract(wiplog.getDoneQty());
                sfcWiplogMapper.updateById(wiplog);
            }
        }
        for (StepScrapVo scrapVo : stepScrapVo) {
            sfcRepair = new SfcRepair();
            sfcRepair.setSite(UserUtils.getSite());
            sfcRepair.setBo(com.itl.iap.common.util.UUID.uuid32());
            sfcRepair.setSfc(scrapVo.getSfc());
            sfcRepair.setRepairTime(date);
            sfcRepair.setScrapQty(scrapVo.getScrapQty());
            sfcRepair.setStationBo(scrapVo.getScrapStationBo());
            sfcRepair.setDutyUser(scrapVo.getUserBo());
            sfcRepair.setDutyOperation(scrapVo.getScrapOperationBo());
            sfcRepair.setNcCodeBo(scrapVo.getNcCodeBo());
            sfcRepair.setStepRepairFlag(BigDecimal.ONE.toString());
            sfcRepairMapper.insert(sfcRepair);

            sfcWiplog = new SfcWiplog();
            BeanUtils.copyProperties(sfcObj,sfcWiplog);
            sfcWiplog.setBo(UUID.uuid32());
            sfcWiplog.setOutTime(new Date());
            sfcWiplog.setInputQty(BigDecimal.ZERO);
            sfcWiplog.setDoneQty(BigDecimal.ZERO);
            sfcWiplog.setNcQty(BigDecimal.ZERO);
            sfcWiplog.setScrapQty(scrapVo.getScrapQty());
            sfcWiplog.setState("出站");
            sfcWiplog.setOperationBo(scrapVo.getScrapOperationBo());
            sfcWiplog.setUserBo(scrapVo.getUserBo());
            sfcWiplog.setStationBo(scrapVo.getScrapStationBo());
            sfcWiplogMapper.insert(sfcWiplog);
        }

        //更新sfc报废数
        Sfc sfcEntity = new Sfc();
        sfcEntity.setScrapQty(sfcObj.getScrapQty().add(scrapQty));
        sfcMapper.update(sfcEntity,new QueryWrapper<Sfc>().eq("sfc",sfc));
    }

    /**
     * 校验是否可以记录报废
     */
    private BigDecimal checkIsCanRecordScrap(Sfc sfcObj,List<StepScrapVo> stepScrapVo) throws CommonException {
        if (Sfc.F_STATE.equals(sfcObj.getState())){
            throw new CommonException("该sfc已完成", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        UserInfo userInfo = sfcServiceImpl.getUserInfo();
        String operationBo = userInfo.getOperationBo();//当前工序
        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", sfcObj.getSfcRouterBo()));
        //验证是否是这条工艺路线上的工序
        if (!routerProcess.getProcessInfo().contains(operationBo)){
            throw new CommonException("当前工序不在该SFC工艺路线上，请重新选择工序", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        BigDecimal scrapTotalQty = BigDecimal.ZERO;//本次登记报废总数
        for (StepScrapVo scrapVo : stepScrapVo) {
            scrapTotalQty = scrapTotalQty.add(scrapVo.getScrapQty());
        }
        if (scrapTotalQty.compareTo(BigDecimal.ZERO) == 0){
            throw new CommonException("报废总数不能为0",504);
        }
        //计算该SFC在此工序的可报废数量
        //可报废条件有两个：1、不能大于该sfc在本工序出站的良品数 2、不能大于该sfc在本工步的可报工数量
        List<SfcWiplog> sfcWipLogs = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("sfc", sfcObj.getSfc()).eq("operation_bo", operationBo));
        BigDecimal doneQty = BigDecimal.ZERO;//在本工序的出站良品数
        for (SfcWiplog sfcWipLog : sfcWipLogs) {
            doneQty = doneQty.add(sfcWipLog.getDoneQty());
        }
        if (scrapTotalQty.compareTo(doneQty) == 1){
            throw new CommonException("报废总数不能大于本工序出站良品总数，数量为" + doneQty.toString(),504);
        }
        BigDecimal canReportTotalQty = reportWorkMapper.getCanReportTotalQty(operationBo, sfcObj.getSfc());//总数
        BigDecimal currentStepReportQty = reportWorkMapper.getCanReportQtyByStep(operationBo, sfcObj.getSfc(), userInfo.getWorkStationBo());//本工步已经报过的数量
        String scarpQty = reportWorkMapper.getStepScrapQtyByWorkStep(userInfo.getWorkStationBo(), sfcObj.getSfc());//工步记录的报废

        //可报工数量为：总数 - 该工步已经报工过的数量 - 工步记录的报废
        BigDecimal canReworkQty = canReportTotalQty.subtract(currentStepReportQty).subtract(new BigDecimal(scarpQty));

        if (scrapTotalQty.compareTo(canReworkQty) == 1){
            throw new CommonException("报废总数不能大于该条码在该工步的可报工数量，数量为"+canReworkQty.toString(),504);
        }
        return scrapTotalQty;
    }
}
