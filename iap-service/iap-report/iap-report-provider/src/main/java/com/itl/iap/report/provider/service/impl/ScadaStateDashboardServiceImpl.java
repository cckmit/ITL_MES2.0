package com.itl.iap.report.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.report.api.service.ScadaStateDashboardService;
import com.itl.iap.report.provider.mapper.ScadaStateMapper;
import com.itl.mes.core.api.dto.ScadaStateDTO;
import com.itl.mes.core.api.entity.ScadaStateEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ScadaStateDashboardServiceImpl extends ServiceImpl<ScadaStateMapper, ScadaStateEntity> implements ScadaStateDashboardService {

    @Autowired
    private ScadaStateMapper scadaStateMapper;


    @Override
    public Map<String,String> getThreeFactoryScadaState() throws CommonException {
        List<ScadaStateDTO> scadaList = scadaStateMapper.selectScadaList();
        Map<String,String> map = new HashMap<>();
        for(ScadaStateDTO scadaStateDTO:scadaList){
            System.out.println(scadaStateDTO);
//            if(scadaStateDTO.getFMachState().equals("1")) {
//                scadaStateDTO.setFMachState("<span class=\"el-icon-circle-check\"></span>");
//            }
//            if(scadaStateDTO.getFMachState().equals("2")) {
//                scadaStateDTO.setFMachState("<span class=\"el-icon-video-play\"></span>");
//            }
//            if(scadaStateDTO.getFMachState().equals("3")) {
//                scadaStateDTO.setFMachState("<span class=\"el-icon-warning-outline\"></span>");
//            }
//            if(scadaStateDTO.getFMachState().equals("4")) {
//                scadaStateDTO.setFMachState("<span class=\"el-icon-setting\"></span>");
//            }
//            if(scadaStateDTO.getFMachState().equals("5")) {
//                scadaStateDTO.setFMachState("<span class=\"el-icon-moon\"></span>");
//            }
//            if(scadaStateDTO.getFMachState().equals("8")) {
//                scadaStateDTO.setFMachState("<span class=\"el-icon-switch-button\"></span>");
//            } else {
//                scadaStateDTO.setFMachState("<span class=\"el-icon-loading\"></span>");
//            }

            map.put(scadaStateDTO.getFMachNo(),scadaStateDTO.getFMachState());
        }
        System.out.println(map);
        return map;
    }
}
