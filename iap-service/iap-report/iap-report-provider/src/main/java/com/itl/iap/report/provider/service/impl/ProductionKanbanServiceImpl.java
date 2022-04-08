package com.itl.iap.report.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.report.api.dto.*;
import com.itl.iap.report.api.entity.AndonException;
import com.itl.iap.report.api.service.AndonReportService;
import com.itl.iap.report.api.service.ProductionKanbanService;
import com.itl.iap.report.api.vo.AndonExceptionVo;
import com.itl.iap.report.api.vo.AndonTypeVo;
import com.itl.iap.report.api.vo.AndonVo;
import com.itl.iap.report.api.vo.AndonWarningVo;
import com.itl.iap.report.provider.mapper.AndonReportMapper;
import com.itl.iap.report.provider.mapper.ProductionKanbanMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ProductionKanbanServiceImpl implements ProductionKanbanService {

    @Resource
    private ProductionKanbanMapper productionKanbanMapper;


    @Override
    public List<Map<String, Object>> selectDeviceState(String name) {
        if(StringUtils.isEmpty(name)){
            try {
                throw new CommonException("",2);
            } catch (CommonException e) {
                e.printStackTrace();
            }
        }
        return productionKanbanMapper.selectDeviceState(name);
    }

    @Override
    public Map<String, Object> selectSynthesize(String name) {
        HashMap<String,Object> result=new HashMap<>(4);
        Integer ProductionWorkers = productionKanbanMapper.selectProductionWorkers(name);
        Integer ProductionWorkersAttendance= productionKanbanMapper.selectProductionWorkersAttendance(name);
        Integer Capacity = productionKanbanMapper.selectCapacity(name);
        List<Map<String,Object>> Capacitys = productionKanbanMapper.selectCapacitys(name);
        //人员
        result.put("person",ProductionWorkers);
        //出勤人员
        result.put("attendancePerson",ProductionWorkersAttendance);
        //日产能
        result.put("dayCapacity ",Capacity);
        //月产能
        result.put("weekCapacity",Capacitys);
        return result;
    }

    @Override
    public IPage<Map<String, Object>> selectPersonQty(Page page, String name) {
        IPage<Map<String, Object>> mapIPage = productionKanbanMapper.selectPersonQty(page, name);
        List<Map<String, Object>> maps = productionKanbanMapper.selectPersonDoneQty(name);
        if(!CollectionUtils.isEmpty(mapIPage.getRecords())){
            mapIPage.getRecords().forEach(x ->{
                maps.forEach(y ->{
                    if(x.get("real_name").equals(y.get("real_name"))){
                        BigDecimal numOne= (BigDecimal) x.get("qty");
                        BigDecimal numTwo= (BigDecimal) y.get("done_qty");
                        x.put("qty",numOne.add(numTwo));
                    }
                });
            });
            return mapIPage;
        }
        return null;
    }


}
