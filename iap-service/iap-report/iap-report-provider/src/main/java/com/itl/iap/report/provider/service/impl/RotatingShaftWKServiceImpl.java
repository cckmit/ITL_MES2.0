package com.itl.iap.report.provider.service.impl;

import com.itl.iap.common.util.CollectorsUtil;
import com.itl.iap.common.util.MapValueComparator;
import com.itl.iap.common.util.MathUtils;
import com.itl.iap.report.api.service.RotatingShaftWKService;
import com.itl.iap.report.provider.mapper.RotatingShaftWorkshopKanbanMapper;
import com.itl.mes.core.api.dto.AnomalousTimeDto;
import com.itl.mes.core.api.vo.LowEfficiencyBatchVO;
import com.itl.mes.core.api.vo.WaitingDocumentsVO;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sekun
 * @since 2021-08-05
 */
@Service
@Transactional
public class RotatingShaftWKServiceImpl implements RotatingShaftWKService {


    @Resource
    private RotatingShaftWorkshopKanbanMapper rotatingShaftWorkshopKanbanMapper;

    //时间格式
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
    SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
    //当前日期
    Date date = new Date();
    //时间处理
    Calendar calendar = Calendar.getInstance();

    @Override
    public HashMap<String, Object> selectBoardQuantity() {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> data1 = rotatingShaftWorkshopKanbanMapper.selectShaft();
        HashMap<String, Object> data2 = rotatingShaftWorkshopKanbanMapper.selectSpiale();
        HashMap<String, Object> data3 = rotatingShaftWorkshopKanbanMapper.selectRotor();
        data.put("shaft", data1);
        data.put("spiale", data2);
        data.put("rotor", data3);
        return data;
    }

    @Override
    public HashMap<String, Object> selectFPY() {
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> data1 = rotatingShaftWorkshopKanbanMapper.selectShaftFPY();
        List<HashMap<String, Object>> data2 = rotatingShaftWorkshopKanbanMapper.selectSpialeFPY();
        List<HashMap<String, Object>> data3 = rotatingShaftWorkshopKanbanMapper.selectRotorFPY();
        data.put("shaft", data1);
        data.put("spiale", data2);
        data.put("rotor", data3);
        return data;
    }

    @Override
    public HashMap<String, Object> selectQuantity() {
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> data1 = rotatingShaftWorkshopKanbanMapper.selectShaftQuantity();
        List<HashMap<String, Object>> data2 = rotatingShaftWorkshopKanbanMapper.selectSpialeQuantity();
        List<HashMap<String, Object>> data3 = rotatingShaftWorkshopKanbanMapper.selectRotorQuantity();
        data.put("shaft", data1);
        data.put("spiale", data2);
        data.put("rotor", data3);
        return data;
    }

    @Override
    public HashMap<String, Object> selectAbnormalInformation() {
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> data1 = rotatingShaftWorkshopKanbanMapper.selectAbnormalInformation();
        HashMap<String, Object> data2 = rotatingShaftWorkshopKanbanMapper.selectAbnormalInformationCollect();
        data.put("abnormalInformation", data1);
        data.put("abnormalInformationCollect", data2);
        return data;
    }

    @Override
    public HashMap<String, Object> selectQuantityMoon() {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> data1 = rotatingShaftWorkshopKanbanMapper.selectShaftQuantityMoon();
        HashMap<String, Object> data2 = rotatingShaftWorkshopKanbanMapper.selectSpialeQuantityMoon();
        HashMap<String, Object> data3 = rotatingShaftWorkshopKanbanMapper.selectRotorQuantityMoon();
        data.put("shaftMoon", data1);
        data.put("spialeMoon", data2);
        data.put("rotorMoon", data3);
        return data;
    }

    @Override
    public List<WaitingDocumentsVO> selectWaitingDocuments() throws ParseException {
        List<WaitingDocumentsVO> result = new ArrayList<>();
        List<WaitingDocumentsVO> data = rotatingShaftWorkshopKanbanMapper.selectWaitingDocuments();
        //分成自检时间和品检时间
        List<WaitingDocumentsVO> dataOne = data.stream().filter(x -> x.getSelfCheckDate() != null).collect(Collectors.toList());
        List<WaitingDocumentsVO> dataTwo = data.stream().filter(x -> x.getSelfCheckDate() == null).collect(Collectors.toList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        //获取String类型的时间
        String createDate = sdf.format(date);
        Date start = sdf.parse(createDate);
        if (!CollectionUtils.isEmpty(dataOne)) {
            dataOne.forEach(x -> {
                Date end = null;
                String endData = sdf.format(x.getSelfCheckDate());
                try {
                    end = sdf.parse(endData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long time = start.getTime() - end.getTime();
                x.setWaitingTime(time / (1000 * 60));
            });
            result.addAll(dataOne);
        }
        if (!CollectionUtils.isEmpty(dataTwo)) {
            dataTwo.forEach(x -> {
                Date end = null;
                String endData = sdf.format(x.getQualityCheckDate());
                try {
                    end = sdf.parse(endData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long time = start.getTime() - end.getTime();
                x.setWaitingTime(time / (1000 * 60));
            });
            result.addAll(dataTwo);
        }
        return result;
    }

    @Override
    public LinkedHashMap<String, Long> selectAverageTimeCheck() {
        List<WaitingDocumentsVO> data1 = rotatingShaftWorkshopKanbanMapper.selectAverageTimeCheckOne();
        List<WaitingDocumentsVO> data2 = rotatingShaftWorkshopKanbanMapper.selectAverageTimeCheckTwo();
        List<WaitingDocumentsVO> data3 = rotatingShaftWorkshopKanbanMapper.selectAverageTimeCheckThree();
        List<WaitingDocumentsVO> data4 = rotatingShaftWorkshopKanbanMapper.selectAverageTimeCheckFour();
        List<WaitingDocumentsVO> data5 = rotatingShaftWorkshopKanbanMapper.selectAverageTimeCheckFive();
        List<WaitingDocumentsVO> data6 = rotatingShaftWorkshopKanbanMapper.selectAverageTimeCheckSix();
        List<WaitingDocumentsVO> data7 = rotatingShaftWorkshopKanbanMapper.selectAverageTimeCheckSeven();
        //获取时间
        LinkedHashMap<String, Long> result = new LinkedHashMap<>(6);
        result.put(getTime(-0), getAvgTime(data1));
        result.put(getTime(-1), getAvgTime(data2));
        result.put(getTime(-2), getAvgTime(data3));
        result.put(getTime(-3), getAvgTime(data4));
        result.put(getTime(-4), getAvgTime(data5));
        result.put(getTime(-5), getAvgTime(data6));
        result.put(getTime(-6), getAvgTime(data7));
        return result;
    }


    private String getTime(int day) {
        calendar.setTime(date);
        //获取String类型的时间
        calendar.add(Calendar.DATE, day);//当前时间减去day
        return sdf.format(calendar.getTime());
    }

    //获取平均时间(分钟)
    private long getAvgTime(List<WaitingDocumentsVO> data) {
        if (!CollectionUtils.isEmpty(data)) {
            long timeCount = 0;
            //循环转换成分钟
            for (int i = 0; i < data.size(); i++) {
                Date start = null;
                String startData = ymd.format(data.get(i).getQualityCheckDate());
                Date end = null;
                String endData = ymd.format(data.get(i).getSelfCheckDate());
                try {
                    end = ymd.parse(endData);
                    start = ymd.parse(startData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long q = (start.getTime() - end.getTime()) / 1000;
                long time = q % 3600 / 60;
                timeCount = timeCount + time;
            }
            return timeCount / data.size();
        } else {
            return 0;
        }
    }


    @Override
    public HashMap<String, Object> selectDisqualified() {
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> data1 = rotatingShaftWorkshopKanbanMapper.selectShaftDisqualified();
        List<HashMap<String, Object>> data2 = rotatingShaftWorkshopKanbanMapper.selectSpialeDisqualified();
        List<HashMap<String, Object>> data3 = rotatingShaftWorkshopKanbanMapper.selectRotorDisqualified();
        data.put("shaft", data1);
        data.put("spiale", data2);
        data.put("rotor", data3);
        return data;
    }

    /**
     * 故障分析
     *
     * @return
     */
    @Override
    public HashMap<String, Object> selectFaultAnalysis() {
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> data1 = rotatingShaftWorkshopKanbanMapper.selectOutageRate();
        List<HashMap<String, Object>> data2 = rotatingShaftWorkshopKanbanMapper.selectFaultCause();
        data.put("outage", data1);
        data.put("fault", data2);
        return data;
    }

    /**
     * 设备看板设备统计
     *
     * @return
     */
    @Override
    public HashMap<String, Object> selectDeviceStatistics() {
        HashMap<String, Object> data = new HashMap<>();
        BigDecimal data1 = rotatingShaftWorkshopKanbanMapper.selectTotalNumberDevices();
        BigDecimal data2 = rotatingShaftWorkshopKanbanMapper.selectDailyCheckRate();
        BigDecimal data3 = rotatingShaftWorkshopKanbanMapper.selectDailyMaintenanceRate();
        Integer data4 = rotatingShaftWorkshopKanbanMapper.selectNumberMonthlyTenance();
        Integer data5 = rotatingShaftWorkshopKanbanMapper.selectNetworkingDevice();
        HashMap<String, Object> data6 = rotatingShaftWorkshopKanbanMapper.selectEquipmentStatus();
        data.put("设备总数", data1);
        data.put("日点检率", data2.compareTo(BigDecimal.ZERO) > 0 ? data2.divide(data1, 2, BigDecimal.ROUND_HALF_UP) : 0);
        data.put("日保养率", data3.compareTo(BigDecimal.ZERO) > 0 ? data3.divide(data1, 2, BigDecimal.ROUND_HALF_UP) : 0);
        data.put("月维修数", data4);
        data.put("联网设备", data5);
        data.put("设备状态", data6);
        return data;
    }

    @Override
    public LinkedHashMap<String, BigDecimal> selectOperatingRatio() {
        LinkedHashMap<String, BigDecimal> data = new LinkedHashMap<>(7);
        List<HashMap<String, Object>> data1 = rotatingShaftWorkshopKanbanMapper.selectOperatingRatio("1");
        List<HashMap<String, Object>> data2 = rotatingShaftWorkshopKanbanMapper.selectOperatingRatio("2");
        data1.forEach(x -> {
            data2.forEach(y -> {
                if (x.get("day").equals(y.get("day"))) {
                    BigDecimal num1 = MathUtils.getBigDecimal(x.get("totalTime"));
                    BigDecimal num2 = MathUtils.getBigDecimal(y.get("totalTime"));
                    BigDecimal num3 = num1.add(num2);
                    data.put((String) x.get("day"), num1.compareTo(BigDecimal.ZERO) > 0 ? num1.divide(num3, 2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0));
                }
            });
        });
        return data;
    }

    @Override
    public List<LowEfficiencyBatchVO> selectLowEfficiencyBatch() {
        List<LowEfficiencyBatchVO> data = rotatingShaftWorkshopKanbanMapper.selectLowEfficiencyBatch();
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(x -> {
                BigDecimal num = new BigDecimal(0.01);
                BigDecimal numOne = DateConversion(x.getOutTime(), x.getInTime());
                if (numOne.compareTo(BigDecimal.ZERO) > 0) {
                    num = numOne;
                }
                x.setTime(num);
                x.setEfficiency(x.getDoneQty().divide(num, 2, BigDecimal.ROUND_HALF_UP));
            });
            data.stream().sorted(Comparator.comparing(LowEfficiencyBatchVO::getEfficiency).reversed()).limit(8);
        }
        return data;
    }

    @Override
    public Map<String, BigDecimal> selectTallEfficiencyBatch() {
        List<LowEfficiencyBatchVO> data = rotatingShaftWorkshopKanbanMapper.selectTallEfficiencyBatch();
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(x -> {
                BigDecimal num = new BigDecimal(0.01);
                BigDecimal numOne = DateConversion(x.getOutTime(), x.getInTime());
                if (numOne.compareTo(BigDecimal.ZERO) > 0) {
                    num = numOne;
                }
                x.setEfficiency(x.getDoneQty().divide(num, 2, BigDecimal.ROUND_HALF_UP));
            });
            Map<String, BigDecimal> scoreAvg = data.stream()
                    .filter(t -> t.getEfficiency() != null)
                    .collect(Collectors.groupingBy(LowEfficiencyBatchVO::getOperationName,
                            CollectorsUtil.averagingBigDecimal(LowEfficiencyBatchVO::getEfficiency, 2, BigDecimal.ROUND_HALF_UP)));
            Map<String, BigDecimal> map3 = new TreeMap<>(new MapValueComparator<>(scoreAvg));
            map3.putAll(scoreAvg);
            return map3;
        }
        return null;
    }

    @Override
    public HashMap<String, Object> selectPerformanceStatistics() {
        HashMap<String, Object> data = new HashMap<>();
        LowEfficiencyBatchVO data1=new LowEfficiencyBatchVO();
        data1.setScope("1");
        List<LowEfficiencyBatchVO> dayEfficiency=rotatingShaftWorkshopKanbanMapper.selectTheEffectiveness(data1);
        data1.setScope("2");
        List<LowEfficiencyBatchVO> monthEfficiency=rotatingShaftWorkshopKanbanMapper.selectTheEffectiveness(data1);
        BigDecimal dayData=new BigDecimal("0.00");
        BigDecimal monthData=new BigDecimal("0.00");
        //处理日效能平均数据
        if(!CollectionUtils.isEmpty(dayEfficiency)){
            dayEfficiency.forEach(x->{
                BigDecimal num = new BigDecimal("0.01");
                BigDecimal numOne = DateConversion(x.getOutTime(), x.getInTime());
                if (numOne.compareTo(BigDecimal.ZERO) > 0) {
                    num = numOne;
                }
                x.setEfficiency(x.getDoneQty().divide(num, 2, BigDecimal.ROUND_HALF_UP));
            });
            dayData  = dayEfficiency.stream().map(vo -> ObjectUtils.isEmpty(vo.getEfficiency())
                    ? new BigDecimal(0) : vo.getEfficiency()).reduce(BigDecimal.ZERO, BigDecimal::add).
                    divide(BigDecimal.valueOf(dayEfficiency.size()), 2, BigDecimal.ROUND_HALF_UP);

        }
        //处理月效能平均数据
        if(!CollectionUtils.isEmpty(monthEfficiency)){
            monthEfficiency.forEach(x->{
                BigDecimal num = new BigDecimal("0.01");
                BigDecimal numOne = DateConversion(x.getOutTime(), x.getInTime());
                if (numOne.compareTo(BigDecimal.ZERO) > 0) {
                    num = numOne;
                }
                x.setEfficiency(x.getDoneQty().divide(num, 2, BigDecimal.ROUND_HALF_UP));
            });
            monthData  = monthEfficiency.stream().map(vo -> ObjectUtils.isEmpty(vo.getEfficiency())
                    ? new BigDecimal(0) : vo.getEfficiency()).reduce(BigDecimal.ZERO, BigDecimal::add).
                    divide(BigDecimal.valueOf(monthEfficiency.size()), 2, BigDecimal.ROUND_HALF_UP);

        }
        //获取产量
        Integer numMonth=rotatingShaftWorkshopKanbanMapper.selectWorkshopProduction(data1);
        data1.setScope("1");
        Integer numDay=rotatingShaftWorkshopKanbanMapper.selectWorkshopProduction(data1);
        //获取不良数量与报废数量
        HashMap<String,BigDecimal> dataOne=rotatingShaftWorkshopKanbanMapper.selectIncomingQtyAndScrapped();
        List<AnomalousTimeDto> dataTwo=rotatingShaftWorkshopKanbanMapper.selectAnomalousTime();
        //异常次数
        int num=0;
        //异常总时长
        BigDecimal sum=new BigDecimal("0.00");
        if(!CollectionUtils.isEmpty(dataTwo)){
            num=dataTwo.size();
            dataTwo.forEach(x->{
                    if(x.getRelieveTime()==null){
                        x.setRelieveTime(new Date());
                    }
                x.setTime(DateConversion(x.getRelieveTime(), x.getTriggerTime()));
            });
            sum = dataTwo.stream().map(vo -> ObjectUtils.isEmpty(vo.getTime()) ? new BigDecimal(0) : vo.getTime()).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        data.put("日效能", dayData);
        data.put("月效能", monthData);
        data.put("日产量", numDay);
        data.put("月产量", numMonth);
        data.put("月报废数", dataOne.get("scrapNum"));
        data.put("月不良数", dataOne.get("ncNum"));
        data.put("异常次数", num);
        data.put("异常总时长", sum);
        return data;
    }

    @Override
    public HashMap<String, Object> selectAveragePerformanceTrend() {
        LinkedHashMap<String,Object> resultData=new LinkedHashMap<>(7);
        List<LowEfficiencyBatchVO> data= rotatingShaftWorkshopKanbanMapper.selectAveragePerformanceTrend();
        //近七天日期
        Date date=new Date();
        LinkedList<String> dateList = new LinkedList<>();
        for (int i=6;i>=0;i--){
            Date dateOne = DateUtils.addDays(new Date(),-i);
            String formatDate = day.format(dateOne);
            dateList.add(formatDate);
        }
        Map<String,List<LowEfficiencyBatchVO>> map;
        if(!CollectionUtils.isEmpty(data)){
            data.forEach(x->{
                BigDecimal num = new BigDecimal("0.01");
                BigDecimal numOne = DateConversion(x.getOutTime(), x.getInTime());
                if (numOne.compareTo(BigDecimal.ZERO) > 0) {
                    num = numOne;
                }
                x.setEfficiency(x.getDoneQty().divide(num, 2, BigDecimal.ROUND_HALF_UP));
                x.setDay(day.format(x.getOutTime()));

            });
            map=data.stream().collect(Collectors.groupingBy(LowEfficiencyBatchVO::getProductLineDesc));
            //遍历map
            for (String key : map.keySet()) {
                List<LowEfficiencyBatchVO> value = map.get(key);
                LinkedHashMap<String,BigDecimal> result = new LinkedHashMap<>();
                if (!CollectionUtils.isEmpty(value)) {
                    //循环取到进七天数据
                    dateList.forEach(x->{
                        BigDecimal monthData = value.stream().filter(y -> y.getDay().equals(x))
                                .map(vo -> ObjectUtils.isEmpty(vo.getEfficiency())
                                        ? new BigDecimal(0) : vo.getEfficiency()).reduce(BigDecimal.ZERO, BigDecimal::add).
                                        divide(BigDecimal.valueOf(value.size()), 2, BigDecimal.ROUND_HALF_UP);
                        result.put(x,monthData);
                    });
                }
                resultData.put(key,result);
            }
        }
        return resultData;
    }

    //转换时间差-小时
    private BigDecimal DateConversion(Date outTime, Date inTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        Date start = null;
        String startData = ymd.format(outTime);
        Date end = null;
        String endData = ymd.format(inTime);
        try {
            end = ymd.parse(endData);
            start = ymd.parse(startData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long q = (start.getTime() - end.getTime());
        long time = q%nd/nh;
        return new BigDecimal(time);
    }

    public static void main(String[] args) throws ParseException {
        Date date=new Date();
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        String date0= day.format(date);
        String date1= day.format(DateUtils.addDays(date,-1));
        String date2= day.format(DateUtils.addDays(date,-2));
        String date3= day.format(DateUtils.addDays(date,-3));
        String date4= day.format(DateUtils.addDays(date,-4));
        String date5= day.format(DateUtils.addDays(date,-5));
        String date6= day.format(DateUtils.addDays(date,-6));

        System.out.println(date0);
        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);
        System.out.println(date4);
        System.out.println(date5);
        System.out.println(date6);
    }
}