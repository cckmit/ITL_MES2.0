package com.itl.mes.pp.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.vo.CustomDataValVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.client.service.CustomDataValService;
import com.itl.mes.pp.api.entity.WorkOrderBinding;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleProductionLineEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleShopOrderEntity;
import com.itl.mes.pp.provider.common.CommonCode;
import com.itl.mes.pp.provider.config.Constant;
import com.itl.mes.pp.provider.exception.CustomException;
import com.itl.mes.pp.provider.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class WorkOrderBindingServiceImpl {

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void add(List<Map<String, Object>> params) {
        //判断开始时间
        judgeStartTime(params);

        //保存绑定   并进行排序
        saveBinding(params);

    }

    @Autowired
    WorkOrderBindingMapper workOrderBindingMapper;

    @Transactional
    public void saveBinding(List<Map<String, Object>> params) {
      //  String site = "1040";
        String site = UserUtils.getSite();
        List<ShopOrderFullVo> shopOrderFullVos = new ArrayList<>();
        params.forEach(stringObjectMap -> {
            ShopOrderFullVo shopOrderFullVo = new ShopOrderFullVo();
            shopOrderFullVo.setShopOrder(stringObjectMap.get("shopOrder")==null?"":stringObjectMap.get("shopOrder").toString());

            try {
                Date fixedDate = stringObjectMap.get("fixedTime") == null ? format.parse("9999-12-01 00:00:00") : format.parse(stringObjectMap.get("fixedTime").toString());
                Date negotiationDate = stringObjectMap.get("negotiationTime") == null ? format.parse("9999-12-01 00:00:00") : format.parse(stringObjectMap.get("negotiationTime").toString());
                shopOrderFullVo.setFixedTime(fixedDate);
                shopOrderFullVo.setNegotiationTime(negotiationDate);
            } catch (ParseException e) {
                e.printStackTrace();
            };

            shopOrderFullVos.add(shopOrderFullVo);

        });

        List<ShopOrderFullVo> collect = shopOrderFullVos.stream().sorted(Comparator.comparing(ShopOrderFullVo::getFixedTime).thenComparing(ShopOrderFullVo::getNegotiationTime)).collect(Collectors.toList());
        String uuid = UUID.randomUUID().toString();
        for(int n=1;n<=collect.size();n++){
            WorkOrderBinding workOrderBinding = new WorkOrderBinding();
            workOrderBinding.setBo(collect.get(n-1).getShopOrder());
            workOrderBinding.setSort(n);
            workOrderBinding.setNo(uuid);
            workOrderBinding.setSite(site);
            workOrderBindingMapper.insert(workOrderBinding);
        }

    }

    private void judgeStartTime(List<Map<String, Object>> list) {

        List<String> itemList = new ArrayList<>();
        list.forEach(stringObjectMap -> {
            if(stringObjectMap.get("planStartDate") == null){
                throw new CustomException(CommonCode.STARTTIME_IS_NULL);
            }
            try {
                Date planStartDate = format.parse(stringObjectMap.get("planStartDate").toString());
                String time = afterNDay(7);
                Date parseTime = df.parse(time);
                if(planStartDate.getTime()>parseTime.getTime()){
                    throw new CustomException(CommonCode.STARTTIME_IS_LONG);
                }
            } catch (ParseException e) {
                throw new CustomException(CommonCode.PARSE_TIME_FAIL);
            }

            if(stringObjectMap.get("item") == null){
                throw new CustomException(CommonCode.ITEM_ISEMPTY);
            }
            itemList.add(stringObjectMap.get("item").toString());
        });

        if(!itemList.isEmpty()){
            itemList.forEach(item->{
                if(!item.equals(itemList.get(0))){
                    throw new CustomException(CommonCode.ITEM_ONLY);
                }
            });
        }
    }

    public  String afterNDay(int n){
        Calendar c=Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,n);
        Date d2=c.getTime();
        String s=df.format(d2);
        return s;
    }

    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id->{
            QueryWrapper<WorkOrderBinding> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("bo",id);
            workOrderBindingMapper.delete(entityWrapper);
        });
    }

    public Object assignedBand(List<Map<String, Object>> params) {

        String site = UserUtils.getSite();

       // List<Map<String, Object>> collect = params.stream().filter(e -> e.get("margin") == null && e.get("no") != null).collect(Collectors.toList());

        List<Map<String, Object>> resultList = new ArrayList<>();
        params.forEach(shopOrder->{
            if(shopOrder.get("margin") != null || shopOrder.get("no") == null){
                Map<String, Object> map = new HashMap<>();
                map.put("site",site);
                map.put("shopOrder",shopOrder.get("shopOrder").toString());
                IPage productLine = workOrderBindingMapper.getProductLine(new Page(1, 10000), map);
                BigDecimal reduce = null;
                if(shopOrder.get("no") == null){
                    reduce = shopOrder.get("orderQty") == null ? new BigDecimal("0.0") : new BigDecimal(shopOrder.get("orderQty").toString());

                }else{
                    reduce = params.stream().filter(e->e.get("no")!=null&& shopOrder.get("no").toString().equals(e.get("no").toString())).map(e -> e.get("orderQty") == null ? new BigDecimal("0.0") : new BigDecimal(e.get("orderQty").toString())).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                //   BigDecimal reduce = params.stream().map(e -> e.get("orderQty") == null ? new BigDecimal("0.0") : new BigDecimal(e.get("orderQty").toString())).reduce(BigDecimal.ZERO, BigDecimal::add);

                List<Map<String, Object>> mapList = productLine.getRecords();

                List<Map<String, Object>> custVals = workOrderBindingMapper.getAllCustVals("MACHINE_PROPERTIES");

                //添加大小机台属性
                addMachineProperties(mapList,custVals,site);

                Map<String, Object> result = new HashMap<>();

                boolean flag = false;
                for(Map<String, Object> e: mapList){
                    String machineProperties = e.get("machineProperties") == null ? "" : e.get("machineProperties").toString();
                    if(StringUtils.isNotBlank(machineProperties)&&Constant.BIG_LINE.equals(machineProperties)&& Constant.ORDER_NUM.compareTo(reduce)<0) {
                        result.put("shopOrder",result);
                        result.put("line",e);
                        flag = true;
                        break;

                    }else if(StringUtils.isNotBlank(machineProperties)&&Constant.SMALL_LINE.equals(machineProperties)&& Constant.ORDER_NUM.compareTo(reduce)>0) {
                        result.put("shopOrder",result);
                        result.put("line",e);
                        flag = true;
                        break;
                    }
                }
                if(!flag && !mapList.isEmpty()){
                    result.put("shopOrder",mapList.get(0));
                }

                resultList.add(result);
            }

        });

        return resultList;
    }

    private IPage getProductLine(List<Map<String, Object>> params, String site, Integer pageNum, Integer pageSize) {
        String shopOrder = params.get(0).get("shopOrder").toString();
        Map<String, Object> map = new HashMap<>();
        //map.put("item", item);
        map.put("site",site);
        map.put("shopOrder",shopOrder);
        Page page = new Page(pageNum, pageSize);
        IPage productLine = workOrderBindingMapper.getProductLine(page, map);
        return productLine;
    }

    private void deleteByOrderQty(List<Map<String, Object>> mapList, BigDecimal reduce) {
        mapList.removeIf(e->{
            String machineProperties = e.get("machineProperties") == null ? "" : e.get("machineProperties").toString();
            if(StringUtils.isNotBlank(machineProperties)&&Constant.BIG_LINE.equals(machineProperties)&& Constant.ORDER_NUM.compareTo(reduce)>0) {
                return true;
            }
            if(StringUtils.isNotBlank(machineProperties)&&Constant.SMALL_LINE.equals(machineProperties)&& Constant.ORDER_NUM.compareTo(reduce)<0) {
                return true;
            }
            return false;
        });
    }

    private void addMachineProperties(List<Map<String, Object>> mapList, List<Map<String, Object>> custVals, String site) {
        mapList.forEach(productLine->{
            String product = productLine.get("productLineCode") == null ? "" : productLine.get("productLineCode").toString();
            String bo = "PL:"+site+","+product;
            List<Map<String, Object>> lines = custVals.stream().filter(e -> bo.equals(e.get("bo").toString())).collect(Collectors.toList());
            if(lines != null && !lines.isEmpty()){
                productLine.put("machineProperties",lines.get(0).get("vals"));
            }
        });
    }

    public Object assignedLine(List<Map<String, Object>> params, Integer page, Integer pageSize) {
        return getProductLine(params, UserUtils.getSite(), page, pageSize);
    }

    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    ScheduleProductionLineMapper scheduleProductionLineMapper;
    @Autowired
    ScheduleShopOrderMapper scheduleShopOrderMapper;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    CustomDataValService customDataValService;
    @Transactional
    public void save(List<Map<String, Object>> params) {
        List<ScheduleShopOrderEntity> scheduleShopOrderEntityList = new ArrayList<>();
        List<WorkOrderBinding> workOrderBindings = workOrderBindingMapper.selectList(new QueryWrapper<WorkOrderBinding>());
        List<String> noFirst = workOrderBindings.stream().filter(e -> e.getSort() != 1).map(WorkOrderBinding::getBo).collect(Collectors.toList());
        params.forEach(productLine->{
            String order = productLine.get("shopOrder") == null ? "" : productLine.get("shopOrder").toString();
            List<WorkOrderBinding> collect = workOrderBindings.stream().filter(e -> order.equals(e.getBo())).collect(Collectors.toList());
            String no = UUID.randomUUID().toString();
            if(collect != null && !collect.isEmpty()){
                no = collect.get(0).getNo();
            }
            ScheduleShopOrderEntity scheduleShopOrder = new ScheduleShopOrderEntity();
            scheduleShopOrder.setScheduleBo(no);
            scheduleShopOrder.setShopOrderBo(order);
            scheduleShopOrderMapper.insert(scheduleShopOrder);
            scheduleShopOrderEntityList.add(scheduleShopOrder);


            //保存工单的自定义数据螺杆组合
            //保存自定义数据
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(productLine.get("bo").toString());
            customDataValRequest.setSite(productLine.get("site").toString());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.SHOP_ORDER.getDataType());
            List<CustomDataValVo> customDataValVoList = new ArrayList<>();
            CustomDataValVo customDataValVo = new CustomDataValVo();
            customDataValVo.setAttribute("screwCombination");
            customDataValVo.setVals(productLine.get("screwCombination")==null?"":productLine.get("screwCombination").toString());
            customDataValVoList.add(customDataValVo);
            customDataValRequest.setCustomDataValVoList(customDataValVoList);
            customDataValService.saveCustomDataVal(customDataValRequest);
        });

        // 删除绑定一组的非第一个
        removeNoFirst(params, noFirst);

        //保存
        saveSchedule(params, scheduleShopOrderEntityList);


    }

    private void saveSchedule(List<Map<String, Object>> params, List<ScheduleShopOrderEntity> scheduleShopOrderEntityList) {

        params.forEach(productLine -> {

            ScheduleEntity scheduleEntity = new ScheduleEntity();
            String order = productLine.get("shopOrder") == null ? "" : productLine.get("shopOrder").toString();
            List<ScheduleShopOrderEntity> collect = scheduleShopOrderEntityList.stream().filter(e -> order.equals(e.getShopOrderBo())).collect(Collectors.toList());
            if(collect != null && !collect.isEmpty()){

                saveScheduleEntity(scheduleEntity, productLine, collect.get(0).getScheduleBo());

                saveScheduleProductionLineEntity(scheduleEntity, productLine);
            }

        });
    }

    private void removeNoFirst(List<Map<String, Object>> params, List<String> noFirst) {
        params.removeIf(e->{
            String order = e.get("shopOrder") == null ? "" : e.get("shopOrder").toString();
            if(StringUtils.isNotBlank(order) && noFirst.contains(order)){
                return true;
            }
            return false;
        });
    }

    private void saveScheduleProductionLineEntity(ScheduleEntity scheduleEntity, Map<String, Object> productLine) {
        ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
        scheduleProductionLineEntity.setScheduleBo(scheduleEntity.getBo());
        scheduleProductionLineEntity.setProductionLineBo(productLine.get("productLine")==null?"":productLine.get("productLine").toString());
        scheduleProductionLineEntity.setQuantity(productLine.get("orderQty") == null ? new BigDecimal("0") : new BigDecimal(productLine.get("orderQty").toString()));
        scheduleProductionLineMapper.insert(scheduleProductionLineEntity);
    }

    private void saveScheduleEntity(ScheduleEntity scheduleEntity, Map<String, Object> productLine, String scheduleBo) {
        String username = UserUtils.getCurrentUser().getUserName();
        String site = UserUtils.getSite();
        scheduleEntity.setScheduleNo(scheduleBo);
        scheduleEntity.setItemBo(productLine.get("item")==null?"":productLine.get("item").toString());
        Map<String, Object> productLineMap = new HashMap<>(2);
        productLineMap.put("site",site);
        productLineMap.put("line",productLine.get("productLine")==null?"":productLine.get("productLine").toString());
        scheduleEntity.setWorkshopBo(workOrderBindingMapper.getWorkshop(productLineMap));
        scheduleEntity.setScheduleType(Constant.AUTO_SCHEDULE_TYPE);
        scheduleEntity.setState(Constant.ScheduleState.CREATE.getValue());
        scheduleEntity.setCreateDate(new Date());
        scheduleEntity.setCreateUser(username);
        scheduleMapper.insert(scheduleEntity);
    }

}
