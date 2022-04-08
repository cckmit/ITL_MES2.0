package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.Attached;
import com.itl.mes.core.api.vo.LowEfficiencyBatchVO;
import com.itl.mes.core.api.vo.WaitingDocumentsVO;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sekun
 * @since 2021-08-05
 */
public interface RotatingShaftWKService{

    HashMap<String,Object> selectBoardQuantity();

    HashMap<String,Object> selectFPY();

    HashMap<String,Object> selectQuantity();

    HashMap<String,Object> selectAbnormalInformation();

    HashMap<String,Object> selectQuantityMoon();

    /**
     * 待检单据
     * @return
     */
    List<WaitingDocumentsVO> selectWaitingDocuments() throws ParseException;

    /**
     * 首检核对平均时长
     * @return
     */
    LinkedHashMap<String,Long> selectAverageTimeCheck();

    /**
     * 不合格工序TOP3
     * @return
     */
    HashMap<String,Object> selectDisqualified();

    /**
     * 故障分析
     * @return
     */
    HashMap<String,Object> selectFaultAnalysis();

    /**
     * 故障分析
     * @return
     */
    HashMap<String,Object> selectDeviceStatistics();

    /**
     * 首检核对平均时长
     * @return
     */
    LinkedHashMap<String, BigDecimal> selectOperatingRatio();

    /**
     * 低效能批次
     * @return
     */
    List<LowEfficiencyBatchVO> selectLowEfficiencyBatch();

    /**
     * 高效能批次
     * @return
     */
    Map<String,BigDecimal> selectTallEfficiencyBatch();

    /**
     * 故障分析
     * @return
     */
    HashMap<String,Object> selectPerformanceStatistics();

    /**
     * 班组平均效能趋势
     * @return
     */
    HashMap<String,Object> selectAveragePerformanceTrend();
}