package com.itl.mes.pp.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.andon.core.client.AndonService;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.pp.api.entity.Strategy;
import com.itl.mes.pp.api.entity.SwitchTimeEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleProductionLineEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleShopOrderEntity;
import com.itl.mes.pp.api.entity.scheduleplan.ResourcesCalendarEntity;
import com.itl.mes.pp.provider.common.CommonCode;
import com.itl.mes.pp.provider.config.Constant;
import com.itl.mes.pp.provider.exception.CustomException;
import com.itl.mes.pp.provider.mapper.*;
import com.itl.mes.pp.provider.util.TimeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AutoScheduleServiceImpl {

    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private StrategyMapper strategyMapper;

    @Transactional
    public void addStrategy(List<Strategy> strategyList) {
        strategyList.forEach(strategy -> {
            strategy.setIsDefault(Constant.IS_DEFAULT);
            if(StringUtils.isNotBlank(strategy.getId())){
                strategyMapper.updateById(strategy);
            }else{
                strategyMapper.insert(strategy);
            }
        });
    }


    public Map<String, Object> queryStrategy(Strategy strategy) {
        strategy.setIsDefault(Constant.IS_DEFAULT);
        List<Strategy> strategyList = strategyMapper.getByWorkshopAndLine(strategy);
        if(strategyList == null || strategyList.isEmpty()){
            Strategy defaultStrategy = new Strategy();
            defaultStrategy.setIsDefault(1);
            strategyList =strategyMapper.getByWorkshopAndLine(defaultStrategy);
        }

        Map<String, Object> result = new HashMap<>();
        strategyList.forEach(strategyMap->{
            result.put(strategyMap.getStrategyKey(),strategyMap);
        });
        return result;
    }


    @Autowired
    ShopOrderMapper shopOrderMapper;

    public Object queryPlanSchedule(Map<String,Object> params, Integer pageNum, Integer pageSize, String type) {
        Page page = new Page(pageNum,pageSize);
        String site = UserUtils.getSite();
   //     String site = "1040";
        params.put("site",site);

        IPage autoList = shopOrderMapper.getAutoList(page, params);
        if(type == null && autoList != null && autoList.getRecords() != null && !autoList.getRecords().isEmpty()){
            List<Map<String, Object>> list = autoList.getRecords();
            list.forEach(map->{
                if(map.get("actStartTime") == null){
                    String no = map.get("no").toString();
                    List<Map<String, Object>> collect = list.stream().filter(e -> e.get("margin") != null && e.get("no") != null && no.equals(e.get("no").toString())).collect(Collectors.toList());
                    if(collect != null && !collect.isEmpty()){
                        map.put("actStartTime",collect.get(0).get("actStartTime"));
                    }
                }

                if(map.get("actStartTime") != null && map.get("actStartTime") instanceof Date)  map.put("actStartTime", sd.format((Date)map.get("actStartTime")));
                if(map.get("actEndTime") != null && map.get("actEndTime") instanceof Date)  map.put("actEndTime", sd.format((Date)map.get("actEndTime")));
                if(map.get("negotiationTime") != null && map.get("negotiationTime") instanceof Date)  map.put("negotiationTime", sd.format((Date)map.get("negotiationTime")));
                if(map.get("fixedTime") != null && map.get("fixedTime") instanceof Date)  map.put("fixedTime", sd.format((Date)map.get("fixedTime")));
                if(map.get("orderDeliveryTime") != null && map.get("orderDeliveryTime") instanceof Date)  map.put("orderDeliveryTime", sd.format((Date)map.get("orderDeliveryTime")));
                //添加自定义数据
                addCustomData(map, site);
            });



            autoList.setRecords(list);
            return autoList;
        }

        List<Map<String, Object>> bindings = shopOrderMapper.getBindingBySite(site);

        List<Map<String, Object>> noFirstBinding = bindings.stream().filter(e -> Integer.valueOf(e.get("sort").toString()) != 1).collect(Collectors.toList());

        List<Map<String, Object>> firstBinding = bindings.stream().filter(e -> Integer.valueOf(e.get("sort").toString()) == 1).collect(Collectors.toList());

        if(noFirstBinding != null && !noFirstBinding.isEmpty()){
            List<String> bo = noFirstBinding.stream().map(e -> e.get("bo").toString()).collect(Collectors.toList());
            if(bo != null && !bo.isEmpty()){
                params.put("bindings",bo);
            }
        }

        IPage list = shopOrderMapper.getList(page, params);

        List<Map<String, Object>> shopOrderList = list.getRecords();

        if(shopOrderList == null || shopOrderList.isEmpty()){
            throw new CustomException(CommonCode.NO_NUMBER);
        }

        for(int n = 0; n< shopOrderList.size(); n++){

            //添加自定义数据
            addCustomData(shopOrderList.get(n), site);

        }

        List<Map<String,Object>> resultList = new ArrayList<>();

        shopOrderList.forEach(shopOrder->{

            resultList.add(shopOrder);

            if(firstBinding != null && !firstBinding.isEmpty()){

                //对绑定的数据进行封装
                marginData(firstBinding,shopOrder,resultList,noFirstBinding,site);

            }
        });

        resultList.forEach(stringObjectMap -> {
            if(stringObjectMap.get("orderDeliveryTime") != null && stringObjectMap.get("orderDeliveryTime") instanceof Date) stringObjectMap.put("orderDeliveryTime",sd.format((Date) stringObjectMap.get("orderDeliveryTime")));
            if(stringObjectMap.get("negotiationTime") != null && stringObjectMap.get("negotiationTime") instanceof Date) stringObjectMap.put("negotiationTime",sd.format((Date) stringObjectMap.get("negotiationTime")));
            if(stringObjectMap.get("fixedTime") != null && stringObjectMap.get("fixedTime") instanceof Date) stringObjectMap.put("fixedTime",sd.format((Date) stringObjectMap.get("fixedTime")));
        });
        list.setRecords(resultList);
        return list;
    }

    private void marginData(List<Map<String, Object>> firstBinding, Map<String, Object> shopOrder, List<Map<String, Object>> resultList, List<Map<String, Object>> noFirstBinding, String site) {
        String  no = "";
        for(Map<String, Object> stringObjectMap : firstBinding){
            if(shopOrder.get("shopOrder").toString().equals(stringObjectMap.get("bo").toString())){
                no = stringObjectMap.get("no").toString();
            }
        }
        if(StringUtils.isNotBlank(no)){
            Map last = resultList.get(resultList.size()-1);
            String finalNo = no;
            List<Map<String, Object>> bind = noFirstBinding.stream().filter(e -> finalNo.equals(e.get("no").toString())).collect(Collectors.toList());
            bind.stream().sorted(Comparator.comparing(e->e.get("sort").toString()));
            last.put("margin",bind.size());
            last.put("no",no);
            bind.forEach(map->{
                Map<String, Object> query = new HashMap<>();
                query.put("site",site);
                Map<String, Object> queryResult = queryOne(query,map.get("bo").toString());
                if(queryResult != null){
                    addCustomData(queryResult, site);
                    queryResult.put("no",finalNo);
                    resultList.add(queryResult);
                }

            });
        }
    }


    private  Map<String, Object> queryOne(Map<String, Object> params, String replaceShopOrderBo) {
        params.put("replaceShopOrderBo",replaceShopOrderBo);
        Page page = new Page(1,1);
        IPage list = shopOrderMapper.getList(page, params);
        List<Map<String, Object>> records = list.getRecords();
        if(records!=null && !records.isEmpty()){
            Map<String, Object> result = records.get(0);
            addCustomData(result, params.get("site").toString());
            return result;
        }
        return null;
    }

    private void addCustomData(Map<String, Object> shopOrder, String site) {
        Map<String,Object> param = new HashMap<>();
        param.put("site",site);
        param.put("bo",shopOrder.get("bo").toString());
        param.put("dataType",CustomDataTypeEnum.SHOP_ORDER.getDataType());
        List<CustomDataAndValVo> customDataAndValVos = shopOrderMapper.selectCustomDataAndValByBoAndDataType(param);
        if(shopOrder.get("planStartDate") != null && shopOrder.get("planStartDate") instanceof Date) shopOrder.put("planStartDate",sd.format((Date)shopOrder.get("planStartDate")));
        if(shopOrder.get("planEndDate") != null && shopOrder.get("planEndDate") instanceof Date) shopOrder.put("planEndDate",sd.format((Date)shopOrder.get("planEndDate")));
        customDataAndValVos.forEach(vo -> {
            shopOrder.put(vo.getCdField(), vo.getVals());
        });
    }

    @Transactional
    public void taskLock(List<Map<String, Object>> paramsList) {

        List<Map<String, Object>> erpLotList = shopOrderMapper.getErpLot(UserUtils.getSite());

        String planStartDate = null;
        for(Map<String, Object> params : paramsList){
            List<Map<String, Object>> collect = erpLotList.stream().filter(e -> e.get("erpLot") != null && !e.get("shopOrder").toString().equals(params.get("shopOrder").toString())).collect(Collectors.toList());
            List<String> erpLot = collect.stream().map(e -> e.get("erpLot").toString()).collect(Collectors.toList());
            if(erpLot != null && collect.contains(params.get("erpLot").toString())){
                throw new CustomException(CommonCode.NO_MORE);
            }

            if(params.get("planStartDate") != null){
                planStartDate = params.get("planStartDate").toString();
            }else if(params.get("actStartTime") != null){
                planStartDate = params.get("actStartTime").toString();
            }

            shopOrderMapper.updateErpLot(params);

            Map<String, Object>  shopOrderMap = shopOrderMapper.getByOrder(params.get("shopOrder").toString());
            if(shopOrderMap != null && !shopOrderMap.isEmpty()){
                Map<String, Object> map = new HashMap<>();
                map.put("id",shopOrderMap.get("id").toString());
                shopOrderMapper.updateOrderById(map);
            }else{
                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("shopOrder",params.get("shopOrder").toString());
                insertMap.put("id",UUID.randomUUID().toString());
                shopOrderMapper.taskLock(insertMap);
            }

 //           String time = afterNDay(3);
 //           try {
//                if(sd.parse(planStartDate).getTime()> sd.parse(time).getTime()){
//                    throw new CustomException(CommonCode.ITEM_OUT);
//                }



//            } catch (ParseException e) {
//                throw new CustomException(CommonCode.FAIL);
//            }
        }
    }

    @Transactional
    public void moveOrder(String shopOrder, Integer type, String productLineBo) {

        Map<String, Object> params = new HashMap<>();

        params.put("site",UserUtils.getSite());

        params.put("productLineBo",productLineBo);

        Page page = new Page(1,10000);

        IPage autoList = shopOrderMapper.getAutoList(page, params);

        List<Map<String, Object>> order =  autoList.getRecords();

        int m = 0;
        Map<String, Object> shopOrderMap = new HashMap<>();
        if(order != null && !order.isEmpty()) {
            for (int n = 0; n < order.size(); n++) {
                if (shopOrder.equals(order.get(n).get("shopOrder").toString())) {
                    m = n;
                    shopOrderMap = order.get(n);
                }
            }

            if(shopOrderMap.get("isLock") != null && Integer.valueOf(shopOrderMap.get("isLock").toString())==1){
                throw new CustomException(CommonCode.LOCK_NO_MOVE);
            }
            if (type > 0) {//向下移动
                Map<String, Object> stringObjectMap = order.get(m + 1);
                if(stringObjectMap.get("isLock") != null && Integer.valueOf(stringObjectMap.get("isLock").toString())==1){
                    throw new CustomException(CommonCode.LOCK_NO_MOVE);
                }
                order.set(m,stringObjectMap);
                order.set(m+1, shopOrderMap);
            } else {//向上移动
                Map<String, Object> stringObjectMap = order.get(m - 1);
                if(stringObjectMap.get("isLock") != null && Integer.valueOf(stringObjectMap.get("isLock").toString())==1){
                    throw new CustomException(CommonCode.LOCK_NO_MOVE);
                }
                String no = stringObjectMap.get("no")==null?"":stringObjectMap.get("no").toString();
                if(StringUtils.isNotBlank(no)){
                    List<Map<String, Object>> collect = order.stream().filter(e -> e.get("no") != null && no.equals(e.get("no").toString())).collect(Collectors.toList());
                    Map<String, Object> firstMap = order.stream().filter(e -> e.get("no") != null && no.equals(e.get("no").toString()) && e.get("margin") != null).findFirst().get();

                    order.set(m,firstMap);
                    order.set(m-collect.size(), shopOrderMap);
                }else{
                    order.set(m,stringObjectMap);
                    order.set(m-1, shopOrderMap);
                }
            }

        }

        autoSchedule(params, Constant.IS_NOT_MOVE, order);

    }

    /*
    isMove 为1  自动排程接口
    isMove 为2  工单移动并自动排程接口
     */
    @Transactional
    public void autoSchedule(Map<String,Object> params, Integer isMove, List<Map<String, Object>> records) {
        //删除以前数据
        shopOrderMapper.delAutoSchedule(params);

        if(isMove.equals(Constant.IS_MOVE)){
            //获取所有工单
            IPage list = (IPage)queryPlanSchedule(params, 1, 10000, "");
            records = list.getRecords();
        }

        //增加交期字段
        addDeliveryTime(records);

        //获取排程策略
        List<Strategy> strategyList = getStrategy(params);

        //按照排程策略进行先排
        String bz  = order(strategyList, records, isMove);

        //t+3日内自动锁定订单
        lockThreeDay(strategyList, records);

        //获取最小的开始时间
        String startTime = getMinStartTime(records,bz==null?0.0:Double.valueOf(bz));

        //去除异常时间
        startTime = delException(startTime,params.get("productLineBo").toString(), records);

        //如果开始时间小于当前时间  那么当前时间为开始时间
        startTime = getStartTimeByCurTime(startTime);

        //获取资源日历
        List<ResourcesCalendarEntity> resourcesCalendarEntityList = getResourceCalender(startTime,records.get(records.size()-1).get("deliveryTime").toString(), params.get("productLineBo").toString());

        //根据开始时间和日历排除最开始的一些日历
        Map<String, Object> newResourcesCalendarEntityList = delResourcesCalendar(startTime, resourcesCalendarEntityList);

        //获取锁定的工单
        List<Map<String, Object>> lockOrder = records.stream().filter(e -> e.get("isLock") != null && StringUtils.isNotBlank(e.get("isLock").toString())).collect(Collectors.toList());

        //根据交期重新计算计划开始时间和结束时间 (主体方法)
        List<Map<String, Object>> sortOrderList = calculateStartTime(records, startTime, newResourcesCalendarEntityList, lockOrder);

        //增加绑定订单的子单
        List<Map<String, Object>> addChildList = addChildOrder(sortOrderList, records);

        //将为空的切换时间换成0
        delNullChangeTime(addChildList);

        //排好的订单存入数据库中
        saveAutoSchedule(addChildList);

    }

    private String getStartTimeByCurTime(String startTime) {
        Date nowDate = new Date();
        try {
            if(nowDate.getTime()>sd.parse(startTime).getTime()) return sd.format(nowDate);
        } catch (ParseException e) {
            throw new CustomException(CommonCode.PARSE_TIME_FAIL);
        }
        return startTime;
    }

    private void delNullChangeTime(List<Map<String, Object>> addChildlist) {
        addChildlist.forEach(e-> {
            if(e.get("changeTime") == null || "".equals((e.get("changeTime").toString()))) e.put("changeTime",0.0);
        });
    }

    private String delException(String startTime, String productLineBo, List<Map<String, Object>> records) {
        List<Record> byLine = andonService.findByLine(productLineBo);
        Double exceptionTime = 0.0;
        String time = null;
        if(byLine != null && !byLine.isEmpty()){
            byLine.sort(Comparator.comparing(e->e.getPlanRepairTime()));
            Record record = byLine.get(byLine.size() - 1);
            Date planRepairTime = record.getPlanRepairTime();
            if(planRepairTime != null){
                try {
                    if(sd.parse(startTime).getTime() > planRepairTime.getTime()){
                        time = startTime;
                    }else{
                        exceptionTime = TimeUtils.getDatePoor(sd.format(planRepairTime),startTime);
                        time = sd.format(planRepairTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{
            time = startTime;
        }
        for(int n=0;n<records.size();n++){
            if(n==0) {
                records.get(n).put("exceptionTime",exceptionTime);
            }else{
                records.get(n).put("exceptionTime",0.0);
            }
        }
        return time;
    }

    private void lockThreeDay(List<Strategy> strategyList, List<Map<String, Object>> records) {
        List<Strategy> collect = strategyList.stream().filter(e -> "t3".equals(e.getStrategyKey())).collect(Collectors.toList());
        if(collect != null && !collect.isEmpty()){
            Integer isUse = collect.get(0).getIsUse();
            if(isUse == 1){
                records.forEach(stringObjectMap -> {
                    String deliveryTime = stringObjectMap.get("deliveryTime").toString();
                    String afterTime = TimeUtils.getAfterTime(sd.format(new Date()), 24 * 3.0);
                    try {
                        if(sd.parse(deliveryTime).getTime()<=sd.parse(afterTime).getTime()){
                            stringObjectMap.put("isLock",1);
                        }
                    } catch (ParseException e) {
                        throw new CustomException(CommonCode.PARSE_TIME_FAIL);
                    }
                });
            }
        }

    }

    private List<Strategy> getStrategy(Map<String, Object> params) {
        Strategy strategy = new Strategy();
        strategy.setProductionLine(params.get("productLineBo").toString());
        strategy.setIsDefault(2);
        List<Strategy> strategyList = strategyMapper.getByWorkshopAndLine(strategy);
        if(strategyList == null || strategyList.isEmpty()){
            Strategy defaultStrategy = new Strategy();
            defaultStrategy.setIsDefault(1);
            strategyList =strategyMapper.getByWorkshopAndLine(defaultStrategy);
        }
        return strategyList;
    }

    private String order(List<Strategy> strategyList, List<Map<String, Object>> records, Integer isMove) {
        String day = null;
        List<Strategy> deliveryTimeCollect = strategyList.stream().filter(e -> "deliveryTimeDesc".equals(e.getStrategyKey())).collect(Collectors.toList());
        List<Strategy> materialOptimizationCollect = strategyList.stream().filter(e -> "materialOptimization".equals(e.getStrategyKey())).collect(Collectors.toList());
        List<Strategy> smallSwitchingTimeCollect = strategyList.stream().filter(e -> "smallSwitchingTime".equals(e.getStrategyKey())).collect(Collectors.toList());

        if(isMove.equals(Constant.IS_MOVE)){
            if(deliveryTimeCollect != null && !deliveryTimeCollect.isEmpty()){
                Integer isUse = deliveryTimeCollect.get(0).getIsUse();
                if(isUse==1){
                    //按照交期进行排序
                    orderByDeliveryTime(records);
                }else if(isUse==2){
                    //按照计划开始时间顺序
                    orderByPlanStartTime(records);
                }else if(isUse==3){
                    //按照计划完成时间到序
                    orderByPlanEndTime(records);
                }
                day = deliveryTimeCollect.get(0).getBz();
            }


            List<Map<String, Object>> result = new ArrayList<>();
            //最小切换时间
            if(smallSwitchingTimeCollect != null && !smallSwitchingTimeCollect.isEmpty()){
                Strategy strategy = smallSwitchingTimeCollect.get(0);
                if(strategy.getIsUse() == 1){ //使用
                    List<String> removeOrderList = new ArrayList<>();
                    //统一工艺特性的排一起   排除或者不排除齐套
                    Map<String, List<Map<String, Object>>> collect = records.stream().filter(e -> e.get("processChar") != null).collect(Collectors.groupingBy(e -> e.get("processChar").toString()));
                    records.forEach(stringObjectMap -> {
                        result.add(stringObjectMap);
                        if(stringObjectMap.get("processChar") != null && collect.containsKey(stringObjectMap.get("processChar").toString())){
                            List<Map<String, Object>> list = collect.get(stringObjectMap.get("processChar").toString());
                            List<Map<String, Object>> noThisOrder = list.stream().filter(e -> !e.get("shopOrder").toString().equals(stringObjectMap.get("shopOrder").toString())).collect(Collectors.toList());
                            if(noThisOrder != null && !noThisOrder.isEmpty()){
                                List<Map<String, Object>> otherList = new ArrayList<>();
                                noThisOrder.forEach(e->{
                                    Map<String, Object> map = new HashMap<>();
                                    map.putAll(e);
                                    map.put("del", "no");
                                    otherList.add(map);
                                });
                                result.addAll(otherList);
                                noThisOrder.forEach(e->removeOrderList.add(e.get("shopOrder").toString()));
                            }
                            //删除
                            collect.remove(stringObjectMap.get("processChar").toString());
                        }
                    });
                    System.out.print(111);

                    result.removeIf(e->{
                        if(removeOrderList.contains(e.get("shopOrder").toString()) && e.get("del") == null) return true;
                        return false;
                    });
                }
            }
            if(!result.isEmpty()) records = result;

            //物料齐套
            if(materialOptimizationCollect != null && !materialOptimizationCollect.isEmpty()){
                Strategy strategy = materialOptimizationCollect.get(0);
                if(strategy.getIsUse() == 1){ //使用

                    List<Map<String, Object>> collect = records.stream().filter(e -> (e.get("isLock")==null || "2".equals(e.get("isLock").toString())) && (e.get("KITTING_STATE") == null || "NG".equals(e.get("KITTING_STATE").toString()))).collect(Collectors.toList());

                    records.removeIf(e -> (e.get("isLock")==null || "2".equals(e.get("isLock").toString())) && (e.get("KITTING_STATE") == null || "NG".equals(e.get("KITTING_STATE").toString())));

                    records.addAll(collect);

                }
            }

        }

        return day;
    }

    private void orderByPlanEndTime(List<Map<String, Object>> records) {
        List<Map<String, Object>> newList = new ArrayList<>();
        records.sort(Comparator.comparing(e->e.get("planEndDate").toString()));
        for(int i = records.size()-1; i>0; i++){
            newList.add(records.get(i));
        }
        records = newList;
    }

    private void orderByPlanStartTime(List<Map<String, Object>> records) {
        records.sort(Comparator.comparing(e->e.get("planStartDate").toString()));
    }

    private void saveAutoSchedule(List<Map<String, Object>> sortOrderList) {
        for(int n = 1; n<= sortOrderList.size(); n++){
            Map<String, Object> stringObjectMap = sortOrderList.get(n-1);
            stringObjectMap.put("orderSort",n);
            stringObjectMap.put("id",UUID.randomUUID().toString());
            try{
                if(stringObjectMap.get("actStartTime") != null && stringObjectMap.get("actStartTime") instanceof String) stringObjectMap.put("actStartTime",sd.parse(stringObjectMap.get("actStartTime").toString()));
                if(stringObjectMap.get("actEndTime") != null && stringObjectMap.get("actEndTime") instanceof String) stringObjectMap.put("actEndTime",sd.parse(stringObjectMap.get("actEndTime").toString()));
                if(stringObjectMap.get("negotiationTime") != null && stringObjectMap.get("negotiationTime") instanceof String) stringObjectMap.put("negotiationTime",sd.parse(stringObjectMap.get("negotiationTime").toString()));
                if(stringObjectMap.get("fixedTime") != null && stringObjectMap.get("fixedTime") instanceof String) stringObjectMap.put("fixedTime",sd.parse(stringObjectMap.get("fixedTime").toString()));
                if(stringObjectMap.get("orderDeliveryTime") != null && stringObjectMap.get("orderDeliveryTime") instanceof String) stringObjectMap.put("orderDeliveryTime",sd.parse(stringObjectMap.get("orderDeliveryTime").toString()));

            }catch(Exception e){
                throw new CustomException(CommonCode.PARSE_TIME_FAIL);
            }

            shopOrderMapper.saveAutoSchedule(stringObjectMap);
        }
    }

    private List<Map<String, Object>> addChildOrder(List<Map<String, Object>> sortOrderList, List<Map<String, Object>> records) {
        List<Map<String, Object>> newList = new ArrayList<>();

        sortOrderList.forEach(order ->{
            newList.add(order);
            if(order.get("no") != null){
                List<Map<String, Object>> collect = records.stream().filter(e -> e.get("margin") == null && e.get("no") != null && order.get("no").toString().equals(e.get("no").toString())).collect(Collectors.toList());
                collect.forEach( l->{
                    newList.add(l);
                });
            }
        });
        return newList;
    }

    private Map<String, Object> delResourcesCalendar(String startTime, List<ResourcesCalendarEntity> resourcesCalendarEntityList) {
        List<ResourcesCalendarEntity> newResourcesCalendarEntityList = new ArrayList<>();

        Map<String, Object> result = new HashMap<>();

        String actStartTime = "";//实际开始时间

        boolean flag = false;
        for(ResourcesCalendarEntity resourcesCalendarEntity : resourcesCalendarEntityList){
            Date date = resourcesCalendarEntity.getDate();
            String format = sd.format(date);
            String planStartTime = format.trim().split(" ")[0] + " " + resourcesCalendarEntity.getStartDateStr() + ":00";
            String planEndTime = format.trim().split(" ")[0] + " " + resourcesCalendarEntity.getEndDateStr() + ":00";
            if(TimeUtils.isEffectiveDate(startTime,planStartTime,planEndTime)){//在区间范围内
                flag = true;
                actStartTime = startTime;
            }
            if(flag) newResourcesCalendarEntityList.add(resourcesCalendarEntity);

        }
        if(!flag){
            //没有中间的   选结束时间大于这个时间点的
            List<ResourcesCalendarEntity> collect = resourcesCalendarEntityList.stream().filter(e -> {
                try {
                    String format = sd.format(e.getDate());
                    String planEndTime = format.trim().split(" ")[0] + " " + e.getEndDateStr() + ":00";
                    Date planEndTimeDate = sd.parse(planEndTime);
                    Date startTimeDate = sd.parse(startTime);
                    if (planEndTimeDate.getTime() >= startTimeDate.getTime()) {
                        return true;
                    }
                    return false;
                } catch (ParseException el) {
                    throw new CustomException(CommonCode.FAIL);
                }

            }).collect(Collectors.toList());

            actStartTime = sd.format(collect.get(0).getDate()).trim().split(" ")[0] + " " + collect.get(0).getStartDateStr() + ":00";
            newResourcesCalendarEntityList.addAll(collect);
        }
        result.put("actStartTime", actStartTime);
        result.put("newResourcesCalendarEntityList", newResourcesCalendarEntityList);
        return result;
    }

    @Autowired
    ResourcesCalendarMapper resourcesCalendarMapper;

    private List<ResourcesCalendarEntity> getResourceCalender(String startTime, String deliveryTime, String productLineBo) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", deliveryTime);
        params.put("nowTime", sd.format(new Date()).trim().split(" ")[0]+" "+"00:00:00");
        params.put("productLineBo", productLineBo);
        List<ResourcesCalendarEntity> resourcesCalendarEntities = resourcesCalendarMapper.getResourceCalender(params);

        if(resourcesCalendarEntities == null || resourcesCalendarEntities.isEmpty()){
            throw new CustomException(CommonCode.NO_RESOURCE);
        }
        return resourcesCalendarEntities;
    }

    private String getMinStartTime(List<Map<String, Object>> records, Double num) {
        records.forEach(stringObjectMap -> {
            getConsumptionTotalTime(stringObjectMap, num);
        });
        return records.get(0).get("startTime").toString();
    }

    @Autowired
    SwitchTimeMapper switchTimeMapper;

    private List<Map<String, Object>>  calculateStartTime(List<Map<String, Object>> records, String startTime, Map<String, Object> result, List<Map<String, Object>> lockOrder) {

        List<ResourcesCalendarEntity> resourcesCalendarEntityList = (List<ResourcesCalendarEntity>)result.get("newResourcesCalendarEntityList");

        //排序好的订单id集合
        List<Map<String, Object>> orderList = new ArrayList<>();

        String planStartTime = result.get("actStartTime").toString();

        int n = 0;

        String processChar = null;

        while (true){
            //将绑定后非第一条的数据的生产时长加到第一条上面
            addConsumptionTimeByNoFirst(records, n);

            //根据最开始的时间将耗费时间进行叠加
            if(records.get(n).get("margin") != null || records.get(n).get("no") == null){

                List<String> shopOrder = orderList.stream().map(e -> e.get("shopOrder").toString()).collect(Collectors.toList());

                if(!shopOrder.contains(records.get(n).get("shopOrder").toString())){
                    planStartTime = addChangeTime(planStartTime,processChar, records, n);
                    Map<String, Object> stringObjectMap = delResourcesCalendar(planStartTime, resourcesCalendarEntityList);

                    //根据开始时间算结束时间
                    String planEndTimeByStartTime = getPlanEndTimeByStartTime(planStartTime, records.get(n), (List<ResourcesCalendarEntity>)stringObjectMap.get("newResourcesCalendarEntityList"));

                    Map<String, Object> conflictOrder = null;
                    //判断结束时间是否和绑定订单的开始时间有冲突
                    if (records.get(n).get("isLock") != null && Integer.valueOf(records.get(n).get("isLock").toString()) == 1) conflictOrder = timeConflict(shopOrder, orderList, planEndTimeByStartTime, lockOrder);

                    if(conflictOrder != null){

                        String endTime = getPlanEndTimeByStartTime(stringObjectMap.get("actStartTime").toString(), conflictOrder, (List<ResourcesCalendarEntity>) stringObjectMap.get("newResourcesCalendarEntityList"));

                        conflictOrder.put("actEndTime", endTime);

                        conflictOrder.put("actStartTime", stringObjectMap.get("actStartTime").toString());

                        //重置开始时间 继续循环当前订单
                        planStartTime =  endTime;
                        processChar = records.get(n).get("processChar")==null?"":records.get(n).get("processChar").toString();

                        //有冲突 先排锁定订单的
                        orderList.add(conflictOrder);

                        continue;

                    }else{

                        if(!shopOrder.contains(records.get(n).get("shopOrder").toString())){

                            //获取结束时间
                            String endTime = getPlanEndTimeByStartTime(stringObjectMap.get("actStartTime").toString(), records.get(n), (List<ResourcesCalendarEntity>) stringObjectMap.get("newResourcesCalendarEntityList"));

                            records.get(n).put("actEndTime", endTime);


                            //修改开始和结束时间
                            records.get(n).put("actStartTime", stringObjectMap.get("actStartTime").toString());

                            //重置开始时间 继续循环当前订单
                            planStartTime =  endTime;
                            processChar = records.get(n).get("processChar")==null?"":records.get(n).get("processChar").toString();

                            //没和锁定订单冲突  排当前订单  并循环下个订单
                            orderList.add(records.get(n));
                        }
                        n++;
                    }
                }else{
                    n++;
                }
            }else{
                n++;
            }

            if(n == records.size()){
                break;
            }
        }
        return orderList;
    }

    private String addChangeTime(String planEndTimeByStartTime1, String processChar, List<Map<String, Object>> records, int n) {
        if(StringUtils.isNotBlank(processChar) && records.get(n).get("processChar")!=null && !processChar.equals(records.get(n).get("processChar").toString())){

            //查找切换时间 processChar 上一个工艺特性   records.get(n).get("processChar") 下一个工艺特性
            QueryWrapper<SwitchTimeEntity> queryWrapper = new QueryWrapper<SwitchTimeEntity>();
            queryWrapper.eq("PROCESS_CHARACTERISTICS",processChar);
            queryWrapper.eq("TARGET_PROCESS_CHARACTERISTICS",records.get(n).get("processChar").toString());
            List<SwitchTimeEntity> switchTimeEntities = switchTimeMapper.selectList(queryWrapper);
            if(switchTimeEntities != null && !switchTimeEntities.isEmpty()){
                BigDecimal b = new BigDecimal(String.valueOf(switchTimeEntities.get(0).getSwitchTime()));
                double d = b.doubleValue();
                double num = (double)Math.round(d*10)/10;

                //加上切换时间的结束时间
                planEndTimeByStartTime1 = TimeUtils.getAfterTime(planEndTimeByStartTime1, num);
                //重置切换时间
                records.get(n).put("changeTime",num);
            }else{
                records.get(n).put("changeTime",0.0);
            }
        }else{
            records.get(n).put("changeTime",0.0);
        }
        return planEndTimeByStartTime1;
    }


    private String replacementTime(Map<String, Object> conflictOrder, List<ResourcesCalendarEntity> resourcesCalendarEntityList) {
        //先调用日历 找到对应的日历和开始时间
        Map<String, Object> stringObjectMap = delResourcesCalendar(conflictOrder.get("actStartTime").toString(), resourcesCalendarEntityList);
        //获取结束时间
        String planEndTimeByStartTime = getPlanEndTimeByStartTime(stringObjectMap.get("actStartTime").toString(), conflictOrder, (List<ResourcesCalendarEntity>) stringObjectMap.get("newResourcesCalendarEntityList"));

        //修改开始和结束时间
        conflictOrder.put("actStartTime", stringObjectMap.get("actStartTime").toString());
        conflictOrder.put("actEndTime", planEndTimeByStartTime);
        //重置开始时间 继续循环当前订单
        return planEndTimeByStartTime;
    }

    private Map<String, Object> timeConflict(List<String> shopOrder, List<Map<String, Object>> orderList, String planEndTimeByStartTime, List<Map<String, Object>> lockOrder) {
        for(Map<String, Object> order : lockOrder){
            if(order.get("margin") != null || order.get("no") == null){
                if(!order.isEmpty() && !shopOrder.contains(order.get("shopOrder").toString())){
                    String startTime = order.get("startTime").toString();
                    try {
                        if(sd.parse(planEndTimeByStartTime).getTime()>sd.parse(startTime).getTime()){
                            return order;//有冲突
                        }
                        return null;
                    } catch (ParseException e) {
                        throw new CustomException(CommonCode.PARSE_TIME_FAIL);
                    }
                }
            }
        }
        return null;
    }

    private String getPlanEndTimeByStartTime(String planStartTime, Map<String, Object> stringObjectMap, List<ResourcesCalendarEntity> resourcesCalendarEntityList) {

        Map<String, String> re = new HashMap<>();


        Double consumptionTime = Double.valueOf(stringObjectMap.get("consumptionTime").toString());

        if(resourcesCalendarEntityList == null || resourcesCalendarEntityList.isEmpty()){
            throw new CustomException(CommonCode.RESOURCE_OUT);
        }

        //地柜调用
        digui(planStartTime,resourcesCalendarEntityList,0,consumptionTime, re);

        return re.get("time");

    }

    private void digui(String planStartTime,List<ResourcesCalendarEntity> resourcesCalendarEntityList, int n, Double consumptionTime, Map<String, String> time) {
        Date date = resourcesCalendarEntityList.get(n).getDate();

        String startDateStr = planStartTime;
        String startEndStr = resourcesCalendarEntityList.get(n).getEndDateStr();

        String startCompareDateStr = sd.format(date).split(" ")[0] + " " + resourcesCalendarEntityList.get(n).getStartDateStr() + ":00";
        String endDateStr = sd.format(date).split(" ")[0] + " " + startEndStr + ":00";
        try {
            if(sd.parse(endDateStr).getTime()<sd.parse(startCompareDateStr).getTime()){//跨日
                endDateStr = TimeUtils.getAfterTime(endDateStr,24.0);
            }
        } catch (ParseException e) {
            throw new CustomException(CommonCode.PARSE_TIME_FAIL);
        }

        double datePoor = TimeUtils.getDatePoor(endDateStr, startDateStr);
        if(datePoor >= consumptionTime){
            time.put("time", TimeUtils.getAfterTime(startDateStr,consumptionTime));
            return;

        }else{
            Date nextDate = resourcesCalendarEntityList.get(n+1).getDate();
            digui(sd.format(nextDate).split(" ")[0]+" "+resourcesCalendarEntityList.get(n+1).getStartDateStr()+":00",resourcesCalendarEntityList,n+1,consumptionTime-datePoor, time);
        }
    }

    private void addConsumptionTimeByNoFirst(List<Map<String, Object>> records, int n) {
        if(records.get(n).get("margin") != null){
            String no = records.get(n).get("no").toString();
            List<Map<String, Object>> collect = records.stream().filter(e -> e.get("no") != null && no.equals(e.get("no").toString()) && e.get("margin")==null).collect(Collectors.toList());
            double production_time = collect.stream().mapToDouble(e -> Double.valueOf(e.get("PRODUCTION_TIME").toString())).sum();
            double consumptionTime = Double.valueOf(records.get(n).get("consumptionTime").toString());
            records.get(n).put("consumptionTime", production_time+consumptionTime);
        }
    }

    private String getConsumptionTotalTime(Map<String, Object> stringObjectMap, Double num) {

        String deliveryTime = stringObjectMap.get("deliveryTime").toString();//交期时间

        if(stringObjectMap.get("PRODUCTION_TIME") == null){
            throw new CustomException(CommonCode.PRODUCT_TIME_EMPTY);
        }

        //都以小时为单位
        String productionTime = stringObjectMap.get("PRODUCTION_TIME").toString();//生产时间
        Double switchTime = 0.0;//切换时间

        //  计划开始生产时间 = 固定交期或协商交期 - 交期固定提前期 - 生产时长 + 前序生产订单占用时长
        Double consumptionTime = switchTime +Double.valueOf(productionTime); //消耗时长
        String startTime = TimeUtils.getAdvanceTime(deliveryTime, consumptionTime+24*num);

        stringObjectMap.put("startTime",startTime);

        stringObjectMap.put("consumptionTime",consumptionTime);

        return startTime;
    }

    private void orderByDeliveryTime(List<Map<String, Object>> records) {
        records.sort(Comparator.comparing(e->e.get("deliveryTime").toString()));
    }

    private void addDeliveryTime(List<Map<String, Object>> records) {
        records.forEach(map->{
            if(map.get("negotiationTime") != null){
                map.put("deliveryTime", map.get("negotiationTime"));
            }else{
                map.put("deliveryTime", map.get("fixedTime"));
            }
        });

        records.forEach(map->{
            if(map.get("deliveryTime") == null){
                throw new CustomException(CommonCode.DELIVERY_TIME_EMPTY);
            }

            if(map.get("no") != null && StringUtils.isNotBlank(map.get("no").toString())){
                String no = map.get("no").toString();
                List<Map<String, Object>> collect = records.stream().filter(e -> e.get("no") != null && no.equals(e.get("no").toString())).collect(Collectors.toList());
                //找到交期最小的
                String deliveryTime = collect.stream().map(e -> e.get("deliveryTime").toString()).min(String::compareTo).get();
                map.put("deliveryTime",deliveryTime);

            }
        });
    }

    @Autowired
    AndonService andonService;

    public Object getExceptionByLine(Map<String, Object> params) {
        String productLineBo = params.get("productLineBo").toString();
        List<Record> byLine = andonService.findByLine(productLineBo);
        List<Map<String, Object>> result = new ArrayList<>();
        if(byLine != null && !byLine.isEmpty()){
            byLine.forEach(record -> {
                Map<String, Object> map = new HashMap<>();
                map.put("type",Constant.andonResourceType.getItemName(record.getResourceType()));
                map.put("reason",record.getAbnormalRemark());
                map.put("line",productLineBo);
                Date planRepairTime = record.getPlanRepairTime();
                Date abnormalTime = record.getAbnormalTime();
                if(planRepairTime != null && abnormalTime != null){
                    map.put("exceptionTime",TimeUtils.getDatePoor(sd.format(planRepairTime),sd.format(abnormalTime)));
                }
                map.putAll(params);
                result.add(map);
            });
        }
        return result;
    }

    public void cancelSchedule(Map<String, Object> params) {
        shopOrderMapper.delAutoSchedule(params);
    }

    @Autowired
    ScheduleShopOrderMapper scheduleShopOrderMapper;

    @Autowired
    ScheduleProductionLineMapper scheduleProductionLineMapper;

    @Autowired
    ScheduleMapper scheduleMapper;

    @Transactional
    public void callOffSchedule(List<String> shopOrderList) {
        shopOrderList.forEach(shopOrder->{
            QueryWrapper<ScheduleShopOrderEntity> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("SHOP_ORDER_BO",shopOrder);
            List<ScheduleShopOrderEntity> scheduleShopOrderEntityList = scheduleShopOrderMapper.selectList(entityWrapper);
            String scheduleBo = null;
            if(scheduleShopOrderEntityList != null && !scheduleShopOrderEntityList.isEmpty()){
                scheduleBo = scheduleShopOrderEntityList.get(0).getScheduleBo();
                scheduleShopOrderEntityList.forEach(scheduleShopOrderEntity -> {
                    scheduleShopOrderMapper.deleteById(scheduleShopOrderEntity.getBo());
                });
            }
            if(StringUtils.isNotBlank(scheduleBo)){
                QueryWrapper<ScheduleProductionLineEntity> lineWrapper = new QueryWrapper<>();
                lineWrapper.eq("SCHEDULE_BO",scheduleBo);
                scheduleProductionLineMapper.delete(lineWrapper);

                QueryWrapper<ScheduleEntity> scheduleWrapper = new QueryWrapper<>();
                scheduleWrapper.eq("SCHEDULE_NO",scheduleBo);
                scheduleMapper.delete(scheduleWrapper);
            }
        });
    }

    @Transactional
    public void issuedOrder(List<String> shopOrderList) {
        shopOrderList.forEach(shopOrder->{
            QueryWrapper<ScheduleShopOrderEntity> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("SHOP_ORDER_BO",shopOrder);
            List<ScheduleShopOrderEntity> scheduleShopOrderEntityList = scheduleShopOrderMapper.selectList(entityWrapper);
            scheduleShopOrderEntityList.forEach(scheduleShopOrderEntity -> {
                QueryWrapper<ScheduleEntity> scheduleWrapper = new QueryWrapper<>();
                scheduleWrapper.eq("SCHEDULE_NO",scheduleShopOrderEntity.getScheduleBo());
                ScheduleEntity scheduleEntity = scheduleMapper.selectOne(scheduleWrapper);
                scheduleEntity.setState(2);
                if(scheduleEntity != null) scheduleMapper.updateById(scheduleEntity);
            });
        });
    }

    public void noIssuedOrder(List<String> shopOrderList) {
        shopOrderList.forEach(shopOrder->{
            QueryWrapper<ScheduleShopOrderEntity> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("SHOP_ORDER_BO",shopOrder);
            List<ScheduleShopOrderEntity> scheduleShopOrderEntityList = scheduleShopOrderMapper.selectList(entityWrapper);
            scheduleShopOrderEntityList.forEach(scheduleShopOrderEntity -> {
                QueryWrapper<ScheduleEntity> scheduleWrapper = new QueryWrapper<>();
                scheduleWrapper.eq("SCHEDULE_NO",scheduleShopOrderEntity.getScheduleBo());
                ScheduleEntity scheduleEntity = scheduleMapper.selectOne(scheduleWrapper);
                scheduleEntity.setState(1);
                if(scheduleEntity != null) scheduleMapper.updateById(scheduleEntity);
            });
        });
    }

    public void noTaskLock(List<Map<String, Object>> paramsList) {

        for(Map<String, Object> params : paramsList){
            Map<String, Object>  shopOrderMap = shopOrderMapper.getByOrder(params.get("shopOrder").toString());
            if(shopOrderMap != null && !shopOrderMap.isEmpty()){
                Map<String, Object> map = new HashMap<>();
                map.put("id",shopOrderMap.get("id").toString());
                shopOrderMapper.updateNoLock(map);
            }
        }
    }

    public Object queryTaskLock(Integer pageNum, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("nowTime", new Date());
        params.put("site", UserUtils.getSite());
        try {
            params.put("afterTime", sd.parse(TimeUtils.getAfterTime(sd.format(new Date()),24.0*3)));
        } catch (ParseException e) {
            throw new CustomException(CommonCode.PARSE_TIME_FAIL);
        }
        Page page = new Page(pageNum, pageSize);
        IPage<Map<String, Object>> order = shopOrderMapper.getOrderByT3(page, params);
        return order;
    }
}
