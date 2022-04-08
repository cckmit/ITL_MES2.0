package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.itl.mes.core.api.dto.SfcDataStatisticsDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.SfcDataStatisticsVo;
import com.itl.mes.core.provider.mapper.SfcWiplogMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class QualityKanbanServiceImpl implements QualityKanbanService {

    @Autowired
    private SfcService sfcService;
    @Autowired
    private WorkShopService workShopService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private RouterProcessService routerProcessService;
    @Autowired
    private SfcWiplogMapper sfcWiplogMapper;
    @Autowired
    private OperationService operationService;
    @Override
    public IPage<SfcDataStatisticsVo> getSfcDataStatistics(SfcDataStatisticsDto sfcDataStatisticsDto) {
        if (ObjectUtil.isEmpty(sfcDataStatisticsDto.getPage())){
            sfcDataStatisticsDto.setPage(new Page(1,10));
        }
        List<Sfc> sfcList = sfcService.page(sfcDataStatisticsDto.getPage(), new QueryWrapper<Sfc>().eq("state", "已完成")).getRecords();
        List<SfcDataStatisticsVo> sfcDataStatisticsVoList = Lists.newArrayList();
        for (Sfc sfc : sfcList) {
            SfcDataStatisticsVo sfcDataStatisticsVo = new SfcDataStatisticsVo();
            WorkShop workShopObj = workShopService.getOne(new QueryWrapper<WorkShop>().eq("bo", sfc.getWorkShopBo()));
            sfcDataStatisticsVo.setWorkShopName(workShopObj.getWorkShopDesc());
            sfcDataStatisticsVo.setSfc(sfc.getSfc());
            Item itemObj = itemService.getOne(new QueryWrapper<Item>().eq("bo", sfc.getItemBo()));
            sfcDataStatisticsVo.setItemName(itemObj.getItemName());
            OperationTime operationAndTime = getOperationAndTime(sfc.getSfcRouterBo(), sfc.getSfc());
            sfcDataStatisticsVo.setOperationCounts(operationAndTime.getOpCounts());
            Operation op = operationService.getOne(new QueryWrapper<Operation>().eq("bo", operationAndTime.getTheLongestOp()));
            sfcDataStatisticsVo.setTheLongestOperation(op.getOperationName());
            sfcDataStatisticsVo.setTheLongestOperationTime(operationAndTime.getTheLongestTime());

            QueryWrapper<SfcWiplog> wipLogQw = new QueryWrapper<>();
            wipLogQw.eq("sfc",sfc.getSfc());
            wipLogQw.eq("operation_bo",sfc.getOperationBo());
            List<SfcWiplog> sfcWipLogs = sfcWiplogMapper.selectList(wipLogQw);
            BigDecimal doneQty = BigDecimal.ZERO;//良品数
            if (CollectionUtil.isNotEmpty(sfcWipLogs)){
                for (SfcWiplog sfcWipLog : sfcWipLogs) {
                    doneQty = doneQty.add(sfcWipLog.getDoneQty()).add(sfcWipLog.getNcQty());
                }
            }
            sfcDataStatisticsVo.setDoneQty(doneQty);
            BigDecimal ncQty = sfc.getSfcQty().subtract(doneQty);//不良数
            sfcDataStatisticsVo.setNcQty(ncQty);
            BigDecimal ncTh;
            if (ncQty.compareTo(BigDecimal.ZERO) != 0){
                ncTh = ncQty.divide(sfc.getSfcQty(),2,ROUND_HALF_UP);
            }else {
                ncTh = new BigDecimal("0");
            }
//            if (ncTh.compareTo(BigDecimal.ZERO) == 1){
//                sfcDataStatisticsVo.setStateLamp("0");
//            }else {
                sfcDataStatisticsVo.setStateLamp("1");
//            }
            sfcDataStatisticsVo.setNcThreshold(ncTh.multiply(new BigDecimal("100")).toString() + "%");
            sfcDataStatisticsVoList.add(sfcDataStatisticsVo);
        }

        IPage<SfcDataStatisticsVo> sfcDataStatisticsVoIPage = new Page<SfcDataStatisticsVo>(1, 10).setRecords(sfcDataStatisticsVoList);
        return sfcDataStatisticsVoIPage;
    }

    /**
     * 拿到该工艺路线下所有的工序总数和最长时长的工序和时长
     * @param routerBo 工艺路线bo
     * @param sfc 批次条码
     * @return
     */
    public OperationTime getOperationAndTime(String routerBo,String sfc){
        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", routerBo));
        String processInfo = routerProcess.getProcessInfo();
        JSONObject jsonObj = JSON.parseObject(processInfo);
        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        List<String> operationList = Lists.newArrayList();
        OperationTime operationTime = new OperationTime();
        long time = 0;
        String theLongestTime = null;
        String theLongestOp = null;
        if (nodeList.size() > 0){
            for (int i = 0; i < nodeList.size(); i++) {
                String operationBo = JSON.parseObject(nodeList.get(i).toString()).getString("operation");
                if (StringUtils.isNotBlank(operationBo)){
                    if (!operationList.contains(operationBo)){
                        operationList.add(operationBo);
                        QueryWrapper<SfcWiplog> wipLogQw = new QueryWrapper<>();
                        wipLogQw.eq("sfc",sfc);
                        wipLogQw.eq("operation_bo",operationBo);
                        wipLogQw.eq("state","出站");
                        List<SfcWiplog> sfcWipLogs = sfcWiplogMapper.selectList(wipLogQw);
                        if (CollectionUtil.isNotEmpty(sfcWipLogs)){
                            Date inTime = sfcWipLogs.get(0).getInTime();
                            Date outTime = sfcWipLogs.get(0).getOutTime();
                            long difTime = (outTime.getTime() - inTime.getTime())/1000;
                            long day = difTime/(24*3600);
                            long hour = difTime%(24*3600)/3600;
                            long min = difTime%3600/60;
                            if (time < difTime){
                                time = difTime;
                                theLongestTime = day + "天" + hour + "小时" + min + "分钟";
                                theLongestOp = operationBo;
                            }
                        }
                    }
                }
            }
        }
        operationTime.setOpCounts(String.valueOf(operationList.size()));
        operationTime.setTheLongestOp(theLongestOp);
        operationTime.setTheLongestTime(theLongestTime);
        return operationTime;
    }
}
