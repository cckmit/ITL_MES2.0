package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.service.RotatingShaftWKService;
import com.itl.mes.core.api.vo.CollectDeviceRunningStatusVO;
import com.itl.mes.core.api.vo.LowEfficiencyBatchVO;
import com.itl.mes.core.api.vo.WaitingDocumentsVO;
import com.itl.mes.core.provider.mapper.RotatingShaftWorkshopKanbanMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sekun
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/shaftBoard")
@Api(tags = " 转轴车间看板")
public class RotatingShaftWKController {

    @Resource
    private RotatingShaftWorkshopKanbanMapper rotatingShaftWorkshopKanban;

    @Resource
    private RotatingShaftWKService rotatingShaftWKService;

     @GetMapping("/selectCDRS")
     public ResponseData<CollectDeviceRunningStatusVO> selectCDRS(){
         return ResponseData.success(rotatingShaftWorkshopKanban.selectCollDeviceRun());
     }

    @GetMapping("/selectProjectExecute")
    public ResponseData<HashMap<String,Object>> selectProjectExecute(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectProjectExecute());
    }

    @GetMapping("/selectProjectComplete")
    public ResponseData<HashMap<String,Object>> selectProjectComplete(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectProjectComplete());
    }

    @GetMapping("/selectBoardQuantity")
    public ResponseData<HashMap<String,Object>> selectBoardQuantity(){
        return ResponseData.success(rotatingShaftWKService.selectBoardQuantity());
    }

    @GetMapping("/selectFPY")
    public ResponseData<HashMap<String,Object>> selectFPY(){
        return ResponseData.success(rotatingShaftWKService.selectFPY());
    }

    @GetMapping("/selectQuantity")
    public ResponseData<HashMap<String,Object>> selectQuantity(){
        return ResponseData.success(rotatingShaftWKService.selectQuantity());
    }


    @GetMapping("/selectAbnormalInformation")
    public ResponseData<HashMap<String,Object>> selectAbnormalInformation(){
        return ResponseData.success(rotatingShaftWKService.selectAbnormalInformation());
    }

    @GetMapping("/selectQuantityMoon")
    public ResponseData<HashMap<String,Object>> selectQuantityMoon(){
        return ResponseData.success(rotatingShaftWKService.selectQuantityMoon());
    }


    /**
     * 品质检验
     * @return
     */
    @GetMapping("/selectProductNumber")
    public ResponseData<List<HashMap<String,Object>>> selectProductNumber(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectProductNumber());
    }

    @GetMapping("/selectRatePassToday")
    public ResponseData<List<HashMap<String,Object>>> selectRatePassToday(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectRatePassToday());
    }

    @GetMapping("/selectWaitingDocuments")
    public ResponseData<List<WaitingDocumentsVO>> selectWaitingDocuments() throws ParseException {
        return ResponseData.success(rotatingShaftWKService.selectWaitingDocuments());
    }

    @GetMapping("/selectAverageTimeCheck")
    public ResponseData<LinkedHashMap<String,Long>> selectAverageTimeCheck(){
        return ResponseData.success(rotatingShaftWKService.selectAverageTimeCheck());
    }

    @GetMapping("/selectMonthlyItems")
    public ResponseData<List<HashMap<String,Object>>> selectMonthlyItems(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectMonthlyItems());
    }

    @GetMapping("/selectMonthlyModel")
    public ResponseData<List<HashMap<String,Object>>> selectMonthlyModel(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectMonthlyModel());
    }

    @GetMapping("/selectDisqualified")
    public ResponseData<HashMap<String,Object>> selectDisqualified(){
        return ResponseData.success(rotatingShaftWKService.selectDisqualified());
    }

    @GetMapping("/selectWorkOrder")
    public ResponseData<HashMap<String,Object>> selectWorkOrder(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectWorkOrder());
    }

    @GetMapping("/selectBeyondWorkOrder")
    public ResponseData<HashMap<String,Object>> selectBeyondWorkOrder(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectBeyondWorkOrder());
    }

    @GetMapping("/selectRateOperation")
    public ResponseData<HashMap<String,Object>> selectRateOperation(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectRateOperation());
    }

    //设备看板

    /**
     * 故障率统计
     * @return
     */
    @GetMapping("/selectFaultAnalysis")
    public ResponseData<HashMap<String,Object>> selectFaultAnalysis(){
        return ResponseData.success(rotatingShaftWKService.selectFaultAnalysis());
    }

    /**
     * 设备统计
     * @return
     */
    @GetMapping("/selectDeviceStatistics")
    public ResponseData<HashMap<String,Object>> selectDeviceStatistics(){
        return ResponseData.success(rotatingShaftWKService.selectDeviceStatistics());
    }

    /**
     * 维修次数统计
     * @return
     */
    @GetMapping("/selectMaintenanceFrequency")
    public ResponseData<List<HashMap<String,Object>>> selectMaintenanceFrequency(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectMaintenanceFrequency());
    }

    /**
     * 首检核对平均时长
     * @return
     */
    @GetMapping("/selectOperatingRatio")
    public ResponseData<LinkedHashMap<String, BigDecimal>> selectOperatingRatio(){
        return ResponseData.success(rotatingShaftWKService.selectOperatingRatio());
    }

    /**
     * 低效能批次
     * @return
     */
    @GetMapping("/selectLowEfficiencyBatch")
    public ResponseData<List<LowEfficiencyBatchVO>> selectLowEfficiencyBatch(){
        return ResponseData.success(rotatingShaftWKService.selectLowEfficiencyBatch());
    }

    /**
     * 高效能批次
     * @return
     */
    @GetMapping("/selectTallEfficiencyBatch")
    public ResponseData<Map<String,BigDecimal>> selectTallEfficiencyBatch(){
        return ResponseData.success(rotatingShaftWKService.selectTallEfficiencyBatch());
    }

    /**
     * 故障分析
     * @return
     */
    @GetMapping("/selectPerformanceStatistics")
    public ResponseData<Map<String,Object>> selectPerformanceStatistics(){
        return ResponseData.success(rotatingShaftWKService.selectPerformanceStatistics());
    }

    /**
     * 班组平均效能趋势
     * @return
     */
    @GetMapping("/selectAveragePerformanceTrend")
    public ResponseData<Map<String,Object>> selectAveragePerformanceTrend(){
        return ResponseData.success(rotatingShaftWKService.selectAveragePerformanceTrend());
    }

    /**
     * 月报废不良
     * @return
     */
    @GetMapping("/selectMonthScrappedTopFive")
    public ResponseData<List<HashMap<String,Object>>> selectMonthScrappedTopFive(){
        return ResponseData.success(rotatingShaftWorkshopKanban.selectMonthScrappedTopFive());
    }
}
