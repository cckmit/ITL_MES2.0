package com.itl.iap.report.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.itl.iap.report.api.dto.CommonDto;
import com.itl.iap.report.api.dto.DeviceDto;
import com.itl.iap.report.api.dto.DeviceEfficientDto;
import com.itl.iap.report.api.dto.DeviceStateDto;
import com.itl.iap.report.api.entity.TScadaData;
import com.itl.iap.report.api.service.DeviceReportService;
import com.itl.iap.report.api.vo.CommonVo;
import com.itl.iap.report.api.vo.DeviceStateVo;
import com.itl.iap.report.api.vo.EquipmentRunningRateVO;
import com.itl.iap.report.provider.mapper.DeviceReportMapper;
import com.itl.iap.report.provider.util.PageUtil;
import com.itl.iap.report.provider.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceReportServiceImpl implements DeviceReportService {
    @Autowired
    private DeviceReportMapper deviceReportMapper;


    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat yms = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date date=new Date();
    private List<EquipmentRunningRateVO> tomorrowData;

    @Override
    public DeviceEfficientDto selectDeviceEfficient() {
        DeviceEfficientDto deviceEfficientDto=new DeviceEfficientDto();
        List<CommonVo> commonVos=deviceState(deviceReportMapper.getAllCount());
        deviceEfficientDto.setCommonVos(commonVos);
        return deviceEfficientDto;
    }

    @Override
    public List<CommonDto> selectRunningSate(DeviceDto deviceDto) throws ParseException {
        List<String> timeList= TimeUtil.getDays(deviceDto.getStartTime(),deviceDto.getEndTime());
        timeList.set(0,deviceDto.getStartTime());
        timeList.add(deviceDto.getEndTime());
        List<CommonDto> list=new ArrayList<>();
        for(int i=0;i<timeList.size()-1;i++){
            List<TScadaData> stringList=deviceReportMapper.selectByTimes(deviceDto.getDevice(),timeList.get(i),timeList.get(i+1));
            if(stringList !=null && stringList.size()>0){
                CommonDto commonDto=new CommonDto();
                commonDto.setName(sdf.format(sdf.parse(timeList.get(i))));
                commonDto.setCommonVoList(handlePercent(stringList));
                list.add(commonDto);
            }
        }
        return list;
    }

    @Override
    public IPage<DeviceStateVo> deviceStateDetail(DeviceDto deviceDto) {
        List<DeviceStateVo> deviceStateVos=deviceReportMapper.deviceStateDetail(deviceDto);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<DeviceStateVo> page=PageUtil.getPage(deviceDto.getPage(),deviceStateVos);
        DecimalFormat df = new DecimalFormat();
        for(DeviceStateVo deviceStateVo:page.getRecords()){
            // 将持续时间，格式化保留三位小数
            if(StringUtils.isNotBlank(deviceStateVo.getContinuedTime()) && !("0".equals(deviceStateVo.getContinuedTime()))){
                BigDecimal bigDecimal=new BigDecimal(deviceStateVo.getContinuedTime());
                deviceStateVo.setContinuedTime(bigDecimal.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue()+"");
            }
        }
        return page;
    }

    @Override
    public IPage<DeviceStateVo> deviceState(DeviceDto deviceDto) {
        IPage<DeviceStateVo> deviceStateVos=deviceReportMapper.deviceState(deviceDto.getPage(),deviceDto);
        return deviceStateVos;
    }

   /* @Override
    public IPage<DeviceStateVo> deviceDetail(DeviceDto deviceDto) {
        return deviceReportMapper.deviceDetail(deviceDto.getPage(),deviceDto);
    }*/

    @Override
    public DeviceStateDto selectStateInfo(DeviceDto deviceDto) {
        DeviceStateDto deviceStateDto=new DeviceStateDto();
        List<Integer> stateCounts=new ArrayList<>();
        List<DeviceStateVo>  deviceStateVos=deviceReportMapper.selectStateInfo(deviceDto);
        List<String> deviceStates=deviceReportMapper.selectAllDeviceState();
        Map<String,Integer> deviceStateMap=new HashMap<>();
         if(deviceStateVos!=null && deviceStateVos.size()>0){
            for(DeviceStateVo deviceStateVo:deviceStateVos){
                deviceStateMap.put(deviceStateVo.getState(),deviceStateVo.getCount());
            }
        }
        for(String state:deviceStates){
            if(deviceStateMap.get(state)==null){
                stateCounts.add(0);
            }else{
                stateCounts.add(deviceStateMap.get(state));
            }
        }
        deviceStateDto.setStateCounts(stateCounts);
        deviceStateDto.setDeviceStates(deviceStates);
        return deviceStateDto;
    }

    @Override
    public IPage<DeviceStateVo> deviceDetail(DeviceDto deviceDto) {
       List<DeviceStateVo> deviceStateVos= deviceReportMapper.deviceDetail(deviceDto);
       com.baomidou.mybatisplus.extension.plugins.pagination.Page<DeviceStateVo> page=PageUtil.getPage(deviceDto.getPage(),deviceStateVos);
       return page;
    }

    @Override
    public PageInfo selectDeviceObjectTime(EquipmentRunningRateVO vo) {
        Date dateTomorrow;
        Date dateYesterday;
        final CountDownLatch latch = new CountDownLatch(2);
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        if (vo != null) {
            if (vo.getStartTime() == null) {
                Date now = new Date();
                now.setHours(0);
                now.setMinutes(0);
                now.setHours(0);
                vo.setStartTime(now);
                vo.setEndTime(DateUtils.addDays(now, +1));
            }
            dateTomorrow = DateUtils.addDays(vo.getStartTime(), -1);
            dateYesterday = DateUtils.addDays(vo.getEndTime(), +1);
        } else {
            dateTomorrow = DateUtils.addDays(date, +1);
            dateYesterday = DateUtils.addDays(date, -1);
        }
        String Tomorrow = sdf.format(dateTomorrow);
        String Yesterday = sdf.format(dateYesterday);

        List<EquipmentRunningRateVO> data = deviceReportMapper.selectDeviceObjectTime(vo);
        //取出设备编码
        if (!CollectionUtils.isEmpty(data)) {
            List<EquipmentRunningRateVO> result = new ArrayList<>();
            Set<String> collect = data.stream().flatMap(x -> {
                if (StringUtils.isEmpty(x.getDevice())) {
                    return null;
                }
                return Arrays.stream(x.getDevice().split(","));
            }).collect(Collectors.toSet());
            final List<EquipmentRunningRateVO>[] tomorrowData = new List[]{new ArrayList<>(100)};
            final List<EquipmentRunningRateVO>[] yesterdayData = new List[]{new ArrayList<>(100)};
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    tomorrowData[0] = deviceReportMapper.selectDeviceObjectTomorrow(collect, Tomorrow);
                    latch.countDown();
                }
            });
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    yesterdayData[0] = deviceReportMapper.selectDeviceObjectYesterday(collect, Yesterday);
                    latch.countDown();
                }
            });
            try {
                latch.await(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadPool.shutdown();
            //把数据拆分成key value形式并按时间排倒叙
            Map<String, List<EquipmentRunningRateVO>> dataOne = data.stream()
                    .sorted(Comparator.comparing(EquipmentRunningRateVO::getCreateDate).reversed())
                    .collect(Collectors.groupingBy(EquipmentRunningRateVO::getDevice));
            //处理出正确的dataOne数据
            if (!CollectionUtils.isEmpty(tomorrowData[0])) {
                tomorrowData[0].forEach(x -> {
                    List<EquipmentRunningRateVO> dataTwo = dataOne.get(x.getDevice());
                    //判断这个设备明天是否有记录
                    if (!CollectionUtils.isEmpty(dataTwo)) {
                        //把这条数据处理 拿到第一条数据
                        EquipmentRunningRateVO rateVO = dataTwo.get(0);
                        //转换成分钟
                        Date start = null;
                        String startData = ymd.format(vo.getEndTime());
                        Date end = null;
                        String endData = ymd.format(rateVO.getCreateDate());
                        try {
                            end = ymd.parse(endData);
                            start = ymd.parse(startData);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long q = (start.getTime() - end.getTime()) / 1000;
                        long time = q % 3600 / 60;
                        //修改分钟属性
                        dataTwo.get(0).setContinuedTime((double) time);
                    }
                });
            }
            if (!CollectionUtils.isEmpty(yesterdayData[0])) {
                yesterdayData[0].forEach(x -> {
                    List<EquipmentRunningRateVO> dataTFour = dataOne.get(x.getDevice());
                    //判断这个设备昨天是否有记录
                    if (!CollectionUtils.isEmpty(dataTFour)) {
                        //把这条数据处理再追加 那最后一条数据
                        List<EquipmentRunningRateVO> dataTwo = dataOne.get(x.getDevice());
                        //判断这个设备明天是否有记录
                        if (!CollectionUtils.isEmpty(dataTwo)) {
                            //把这条数据处理或追加 拿到最后一条数据
                            EquipmentRunningRateVO rateVO = dataTwo.get(dataTwo.size() - 1);
                            //转换成分钟
                            Date start = null;
                            String startData = ymd.format(rateVO.getCreateDate());
                            Date end = null;
                            String endData = ymd.format(vo.getStartTime());
                            try {
                                end = ymd.parse(endData);
                                start = ymd.parse(startData);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long q = (start.getTime() - end.getTime()) / 1000;
                            long time = q % 3600 / 60;
                            //修改分钟属性
                            dataTwo.get(dataTwo.size() - 1).setContinuedTime((double) time);
                            dataTwo.get(dataTwo.size() - 1).setFMachState(x.getFMachState());
                        }
                    }
                });
            }
            //处理完数据后做聚合
            for (Map.Entry<String, List<EquipmentRunningRateVO>> entry : dataOne.entrySet()) {
                List<EquipmentRunningRateVO> list = entry.getValue();
                //取第一条用
                EquipmentRunningRateVO err = list.get(0);
                //运行时长
                double runningTime = list.stream().filter(x -> x.getFMachState().equals("1")).mapToDouble(EquipmentRunningRateVO::getContinuedTime).sum();
                //待机时长
                Double standbyTime = list.stream().filter(x -> x.getFMachState().equals("2")).mapToDouble(EquipmentRunningRateVO::getContinuedTime).sum();
                standbyTime=new BigDecimal(standbyTime).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
                //报警时长
                Double alarmTime = list.stream().filter(x -> x.getFMachState().equals("3")).mapToDouble(EquipmentRunningRateVO::getContinuedTime).sum();
                alarmTime=new BigDecimal(alarmTime).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
                //关机或断网时长
                Double shutdownTime = list.stream().filter(x -> x.getFMachState().equals("4")).mapToDouble(EquipmentRunningRateVO::getContinuedTime).sum();
                shutdownTime=new BigDecimal(shutdownTime).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
                //调试时长
                Double debuggingTime = list.stream().filter(x -> x.getFMachState().equals("5")).mapToDouble(EquipmentRunningRateVO::getContinuedTime).sum();
                debuggingTime=new BigDecimal(debuggingTime).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
                //时间稼动率
                double sumTime = standbyTime + alarmTime + runningTime + debuggingTime;
                Double TimeKineticRate = sumTime == 0 ? 0.0 : (runningTime / sumTime)*100;
                TimeKineticRate=new BigDecimal(TimeKineticRate).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
                err.setOperationTime(runningTime).setStandbyTime(standbyTime).setTimeRate(TimeKineticRate)
                        .setRiskTime(alarmTime).setCloseTime(shutdownTime).setDebugTime(debuggingTime);
                err.setOperationTime(new BigDecimal(err.getOperationTime()).setScale(3,BigDecimal.ROUND_UP).doubleValue());
                err.setContinuedTime(new BigDecimal(err.getContinuedTime()).setScale(3,BigDecimal.ROUND_UP).doubleValue());
                result.add(err);
            }
            com.baomidou.mybatisplus.extension.plugins.pagination.Page page2 = vo.getPage();
            Page page1 = new Page((int) page2.getCurrent(), (int) page2.getSize());
            int totle = result.size();
            page1.setTotal(totle);
            int startIndex = Math.toIntExact((page2.getCurrent() - 1) * page2.getSize());
            int endIndex = Math.toIntExact(Math.min(startIndex + page2.getSize(), totle));
            page1.addAll(result.subList(startIndex, endIndex));
            return new PageInfo(page1);
        }
        return null;
    }

    @Override
    public IPage<EquipmentRunningRateVO> selectDeviceObject(EquipmentRunningRateVO vo) {
        if (vo.getStartTime() == null){
            Date now=new Date();
            now.setHours(0);
            now.setMinutes(0);
            now.setHours(0);
            vo.setStartTime(now);
            vo.setEndTime(DateUtils.addDays(now,+1));
        }
        return deviceReportMapper.selectDeviceObject(vo.getPage(),vo);
    }

    List<CommonVo> handlePercent(List<TScadaData> list) throws ParseException {
        List<CommonVo> commonVos=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 第一查询list中第一个日期的前一天的最后一个状态
        TScadaData tScadaData= list.get(0);
        Date oneTime=tScadaData.getFMachCreateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oneTime);
        calendar.add(Calendar.DATE,-1);
        String lastDate=sdf.format(calendar.getTime()); // 昨日
        // 获取lastDate 日期中的最后一个状态
        String state=deviceReportMapper.selectMaxTimeByDate(lastDate);
        // 一天中第一个状态
        CommonVo commonVoo=new CommonVo();
        Date time1=sdft.parse(sdf.format(tScadaData.getFMachCreateTime())+" 00:00:00");
        double value1=Double.parseDouble((list.get(0).getFMachCreateTime().getTime()-time1.getTime())+"")/60000;
        BigDecimal bg1 = new BigDecimal(value1);
        double values1=bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal perBg1 = new BigDecimal(values1/60);
        double per1=perBg1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
        if(state==null){
            // 如果state==null ，则前一天无数据，则为停机状态
            commonVoo.setName("停机");
        }else {
            if(state.equals("0")){
                commonVoo.setName("停机");
            }else if(state.equals("1")){
                commonVoo.setName("运行");
            }else if(state.equals("2")){
                commonVoo.setName("待料");
            }else {
                commonVoo.setName("故障");
            }
        }

        commonVoo.setValue(values1);
        commonVoo.setPer(per1);
        commonVos.add(commonVoo);

        for(int i=0;i<list.size();i++){
            CommonVo commonVo=new CommonVo();
            // 一日中最后一个时间点
            if(i==list.size()-1){
                Date date=list.get(i).getFMachCreateTime();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date);
                calendar2.add(Calendar.DATE,1);
                Date perDate=sdft.parse(sdf.format(calendar2.getTime())+" 00:00:00"); // 明日
                double value=Double.parseDouble((perDate.getTime()-list.get(i).getFMachCreateTime().getTime())+"")/60000;
                BigDecimal bg = new BigDecimal(value);
                double values=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                BigDecimal perBg = new BigDecimal(values/60);
                double per=perBg.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();;
                commonVo.setName(list.get(i).getFMachState());
                commonVo.setValue(values);
                commonVo.setPer(per);
            }else {
               double value= Double.parseDouble((list.get(i+1).getFMachCreateTime().getTime()-list.get(i).getFMachCreateTime().getTime())+"")/60000;
                BigDecimal bg = new BigDecimal(value);
                double values=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                BigDecimal perBg = new BigDecimal(values/60);
                double per=perBg.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();;
                commonVo.setName(list.get(i).getFMachState());
                commonVo.setValue(values);
                commonVo.setPer(per);
            }
            if(commonVo.getName()!=null){
                if(commonVo.getName().equals("0")){
                    commonVo.setName("停机");
                }else if(commonVo.getName().equals("1")){
                    commonVo.setName("运行");
                }else if(commonVo.getName().equals("2")){
                    commonVo.setName("待料");
                }else {
                    commonVo.setName("故障");
                }
                commonVos.add(commonVo);
            }


        }
        return commonVos;
    }
    List<CommonVo> deviceState(Map<String,Integer> map){
        List<CommonVo> commonVos=new ArrayList<>();
        map.forEach((key,value)->{
            if(!key.equals("allCount")){
                CommonVo commonVo=new CommonVo();
                commonVo.setName(key);
                double count=value.doubleValue();
                commonVo.setValue(count/map.get("allCount").doubleValue());
                commonVos.add(commonVo);
            }

        });

        return commonVos;
    }
}
