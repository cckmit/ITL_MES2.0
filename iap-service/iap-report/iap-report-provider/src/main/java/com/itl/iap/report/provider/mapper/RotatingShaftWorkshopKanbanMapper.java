package com.itl.iap.report.provider.mapper;

import com.itl.mes.core.api.dto.AnomalousTimeDto;
import com.itl.mes.core.api.vo.CollectDeviceRunningStatusVO;
import com.itl.mes.core.api.vo.LowEfficiencyBatchVO;
import com.itl.mes.core.api.vo.WaitingDocumentsVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface RotatingShaftWorkshopKanbanMapper {

    CollectDeviceRunningStatusVO selectCollDeviceRun();

    /**
     * 三日开工率
     * @return
     */
    HashMap<String,Object> selectProjectExecute();

    /**
     * 三日完工率
     * @return
     */
    HashMap<String,Object> selectProjectComplete();

    /**
     * 泵轴产量数据
     * @return
     */
    //@SqlParser(filter=true)
    HashMap<String,Object> selectShaft();

    /**
     * 转轴
     * @return
     */
    //@SqlParser(filter=true)
    HashMap<String,Object> selectSpiale();

    /**
     * 转子
     * @return
     */
    //@SqlParser(filter=true)
    HashMap<String,Object> selectRotor();

    /**
     * 泵轴直通率
     * @return
     */
    List<HashMap<String,Object>> selectShaftFPY();

    /**
     * 转轴直通率
     * @return
     */
    List<HashMap<String,Object>> selectSpialeFPY();

    /**
     * 转子直通率
     * @return
     */
    List<HashMap<String,Object>> selectRotorFPY();

    /**
     * 泵轴产能数量
     * @return
     */
    List<HashMap<String,Object>> selectShaftQuantity();

    /**
     * 转轴产能数量
     * @return
     */
    List<HashMap<String,Object>> selectSpialeQuantity();

    /**
     * 转子产能数量
     * @return
     */
    List<HashMap<String,Object>> selectRotorQuantity();

    /**
     * 安灯异常信息
     * @return
     */
    List<HashMap<String,Object>> selectAbnormalInformation();

    /**
     * 安灯异常信息汇总
     * @return
     */
    HashMap<String,Object> selectAbnormalInformationCollect();

    /**
     * 泵轴工单月数量
     * @return
     */
    HashMap<String,Object> selectShaftQuantityMoon();

    /**
     * 转轴工单月数量
     * @return
     */
    HashMap<String,Object> selectSpialeQuantityMoon();

    /**
     * 转子工单月数量
     * @return
     */
    HashMap<String,Object> selectRotorQuantityMoon();

    /**
     * 品质检验次数
     * @return
     */
    List<HashMap<String,Object>> selectProductNumber();

    /**
     * 品质检验次数合格率
     * @return
     */
    List<HashMap<String,Object>> selectRatePassToday();

    /**
     * 待检单据
     * @return
     */
    List<WaitingDocumentsVO> selectWaitingDocuments();

    /**
     * 首检核对平均时长当天
     * @return
     */
    List<WaitingDocumentsVO> selectAverageTimeCheckOne();
    /**
     * 首检核对平均时长-1天
     * @return
     */
    List<WaitingDocumentsVO> selectAverageTimeCheckTwo();
    /**
     * 首检核对平均时长-2天
     * @return
     */
    List<WaitingDocumentsVO> selectAverageTimeCheckThree();
    /**
     * 首检核对平均时长-3天
     * @return
     */
    List<WaitingDocumentsVO> selectAverageTimeCheckFour();
    /**
     * 首检核对平均时长-4天
     * @return
     */
    List<WaitingDocumentsVO> selectAverageTimeCheckFive();
    /**
     * 首检核对平均时长-5天
     * @return
     */
    List<WaitingDocumentsVO> selectAverageTimeCheckSix();
    /**
     * 首检核对平均时长-6天
     * @return
     */
    List<WaitingDocumentsVO> selectAverageTimeCheckSeven();

    /**
     * 月检验不良项目TOP5
     * @return
     */
    List<HashMap<String,Object>> selectMonthlyItems();

    /**
     * 月检验不良型号TOP5
     * @return
     */
    List<HashMap<String,Object>> selectMonthlyModel();

    /**
     * 泵轴工单月数量
     * @return
     */
    List<HashMap<String,Object>> selectShaftDisqualified();

    /**
     * 转轴工单月数量
     * @return
     */
    List<HashMap<String,Object>> selectSpialeDisqualified();

    /**
     * 转子工单月数量
     * @return
     */
    List<HashMap<String,Object>> selectRotorDisqualified();

    //工单进度看板
    /*
    在制工单数
     */
    HashMap<String,Object> selectWorkOrder();

    /*
    超期工单数
     */
    HashMap<String,Object> selectBeyondWorkOrder();

    /*
    开工率
     */
    HashMap<String,Object> selectRateOperation();

    //设备看板

    /**
     * 停机率top5
     * @return
     */
    List<HashMap<String,Object>> selectOutageRate();

    /**
     * 故障原因top3
     * @return
     */
    List<HashMap<String,Object>> selectFaultCause();

    /**
     * 设备总数
     * @return
     */
    BigDecimal selectTotalNumberDevices();

    /**
     * 日点检率
     * @return
     */
    BigDecimal selectDailyCheckRate();

    /**
     * 日保养率
     * @return
     */
    BigDecimal selectDailyMaintenanceRate();

    /**
     * 月维修数
     * @return
     */
    Integer selectNumberMonthlyTenance();

    /**
     * 联网设备
     * @return
     */
    Integer selectNetworkingDevice();

    /**
     * 设备状态(运行-待机-离线-报警)
     * @return
     */
    HashMap<String,Object> selectEquipmentStatus();

    /**
     * 设备维修次数)
     * @return
     */
    List<HashMap<String,Object>> selectMaintenanceFrequency();

    /**
     * 设备开关机统计
     * @return
     */
    List<HashMap<String,Object>> selectOperatingRatio(String num);

    //绩效看板

    /**
     * 低效能批次
     * @return
     */
    List<LowEfficiencyBatchVO> selectLowEfficiencyBatch();

    /**
     * 高效能批次
     * @return
     */
    List<LowEfficiencyBatchVO> selectTallEfficiencyBatch();

    /**
     * 转轴车间日效能(1-日 2-月)
     * @return
     */
    List<LowEfficiencyBatchVO> selectTheEffectiveness(@Param("vo") LowEfficiencyBatchVO lowEfficiencyBatchVO);

    /**
     * 转轴车间产量(1-日 2-月)
     * @param scope
     * @return
     */
    Integer selectWorkshopProduction(@Param("vo") LowEfficiencyBatchVO lowEfficiencyBatchVO);

    /**
     * 转轴车间月不良数量and转轴车间月报废数量
     * @param scope
     * @return
     */
    HashMap<String,BigDecimal> selectIncomingQtyAndScrapped();

    /**
     * 获取异常次数与时长
     * @return
     */
    List<AnomalousTimeDto> selectAnomalousTime();

    /**
     * 获取产线前6天数据
     * @return
     */
    List<LowEfficiencyBatchVO> selectProductionEfficiencySeven(int day);


    /**
     * 获取产线当天数据
     * @return
     */
    List<LowEfficiencyBatchVO> selectProductionEfficiencyToday();

    /**
     * 班组平均效能趋势
     * @return
     */
    List<LowEfficiencyBatchVO> selectAveragePerformanceTrend();

    /**
     * 月报废前5
     * @return
     */
    List<HashMap<String,Object>> selectMonthScrappedTopFive();
}
