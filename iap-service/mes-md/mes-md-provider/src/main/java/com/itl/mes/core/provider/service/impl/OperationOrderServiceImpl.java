package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.dto.DispatchDTO;
import com.itl.mes.core.api.dto.OperationOrderDTO;
import com.itl.mes.core.api.entity.Dispatch;
import com.itl.mes.core.api.entity.OperationOrder;
import com.itl.mes.core.api.service.DispatchService;
import com.itl.mes.core.api.service.OperationOrderService;
import com.itl.mes.core.api.service.RouteStationService;
import com.itl.mes.core.provider.mapper.DispatchMapper;
import com.itl.mes.core.provider.mapper.OperationOrderMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OperationOrderServiceImpl extends ServiceImpl<OperationOrderMapper, OperationOrder> implements OperationOrderService {
    @Autowired
    private RouteStationService routeStationService;
    @Autowired
    private OperationOrderMapper operationOrderMapper;
    @Autowired
    private DispatchService dispatchService;
    @Autowired
    private DispatchMapper dispatchMapper;
    @Override
    public IPage<OperationOrder> selectOperationOrder(OperationOrderDTO operationOrderDTO) {
        if (ObjectUtil.isEmpty(operationOrderDTO.getPage())){
            operationOrderDTO.setPage(new Page(0,10));
        }
//        List<String> orderBos = new ArrayList<>();
//        for(String operationBo:operationOrderDTO.getOperationBos()){
//            List<String> subOrderBos=getOrderBos(operationBo);
//            if(subOrderBos !=null){
//                orderBos.addAll(subOrderBos);
//            }
//        }

        operationOrderDTO.setOperationBos(operationOrderDTO.getOperationBos());
        /*QueryWrapper<OperationOrder> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isEmpty(orderBos)){
            queryWrapper.in("bo","");
        }
        queryWrapper.in("bo",orderBos);
        queryWrapper.like("workshop",operationOrderDTO.getWorkShop());
        if (StringUtils.isNotBlank(operationOrderDTO.getOperationOrder())){
            queryWrapper.like("operation_order",operationOrderDTO.getOperationOrder());
        }
        if (StringUtils.isNotBlank(operationOrderDTO.getStartDate()) && StringUtils.isNotBlank(operationOrderDTO.getEndDate())){
            queryWrapper.ge("create_date",operationOrderDTO.getStartDate());
            queryWrapper.le("create_date",operationOrderDTO.getEndDate());
        }
        queryWrapper.notLike("OPERATION_ORDER_STATE","2");
        queryWrapper.orderByDesc("create_date");*/
//        List<OperationOrder> records = operationOrderMapper.selectPage(operationOrderDTO.getPage(), queryWrapper).getRecords();
//        for (int i = 0; i < records.size(); i++) {//筛选派工完成的数据，完成则不返回（完成 = 该工序工单在该工序的派工总数量 = 工序工单数量）
//            QueryWrapper<Dispatch> wrapper = new QueryWrapper<>();
//            wrapper.eq("operation_order",records.get(i).getOperationOrder());
//            wrapper.eq("operation_bo",operationOrderDTO.getOperationBo());
//            List<Dispatch> dispatchList = dispatchService.list(wrapper);
//            BigDecimal dispatchedQty = BigDecimal.ZERO;//已派工数量
//            for (Dispatch dispatch : dispatchList) {
//                dispatchedQty = dispatchedQty.add(dispatch.getDispatchQty());
//            }
//            if (records.get(i).getOperationOrderQty().compareTo(dispatchedQty) == 0){//说明派完了，删除这条数据
//                records.remove(i);
//                i--;
//                continue;
//            }
//        }
        //operationOrderMapper.selectOperationOrder(operationOrderDTO.getPage(),operationOrderDTO);
        //return operationOrderMapper.selectPage(operationOrderDTO.getPage(),queryWrapper);
        if (operationOrderDTO.getWorkShopName().equals(OperationOrderDTO.WORK_SHOP_NAME_)){
            operationOrderDTO.setFlag(OperationOrderDTO.FLAG_);
        }
        IPage<OperationOrder> operationOrderIPage = operationOrderMapper.selectOperationOrder(operationOrderDTO.getPage(), operationOrderDTO);
        //计算可派工数量
        for (OperationOrder record : operationOrderIPage.getRecords()) {
            DispatchDTO dispatchDTO = new DispatchDTO();
            dispatchDTO.setOperationBo(record.getOperationBo());
            dispatchDTO.setOperationOrder(record.getOperationOrder());
            List<Dispatch> dispatchList = dispatchMapper.okDispatchList(dispatchDTO);
            BigDecimal canDispatchQty = record.getOperationOrderQty();//可派工数量
            for (Dispatch dispatch : dispatchList) {
                canDispatchQty = canDispatchQty.subtract(dispatch.getDispatchQty());
            }
            record.setCanDispatchQty(canDispatchQty);
        }
        return operationOrderIPage;
    }

    /**
     * 筛选出需要派工的工序工单（当前工序），拿出工序工单BO放入list集合中
     */
    public List<String> getOrderBos(String operationBo){
        //查询所有的工序工单号
        List<OperationOrder> orderList = operationOrderMapper.selectList(new QueryWrapper<>());
        List<String> orderBos = new ArrayList<>();
        //筛选出该工序下所有的工序工单(拿出晒选出的工序工单号再做查询)
        orderList.forEach(
                operationOrder -> {
                    String processInfo = routeStationService.queryRouterOperation(operationOrder.getRoutre(),operationOrder.getVersion());
                    if (StringUtils.isNotBlank(processInfo)){
                        JSONObject jsonObj = JSON.parseObject(processInfo);
                        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
                        if (nodeList.size() > 0){
                            for (int i=0;i<nodeList.size();i++){
                                JSONObject operationObj = JSON.parseObject(nodeList.get(i).toString());
                                //获取工序
                                String operation = operationObj.getString("operation");
                                if (StringUtils.isNotBlank(operation)){
                                    if (operation.equals(operationBo)){//如果该工序存在，说明这条工序工单是我们要的数据
                                        orderBos.add(operationOrder.getBo());
                                        break;//跳出当前循环，筛选下一条工序工单
                                    }
                                }
                            }
                        }
                    }
                }
        );
        return orderBos;
    }
}
