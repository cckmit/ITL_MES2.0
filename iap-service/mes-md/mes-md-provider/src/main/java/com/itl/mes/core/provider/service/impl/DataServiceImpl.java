package com.itl.mes.core.provider.service.impl;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.andon.core.client.AndonService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.CallTypeDTO;
import com.itl.mes.andon.api.entity.CallType;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.vo.WorkShopVo;
import com.itl.mes.core.provider.mapper.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DataServiceImpl{

    @Autowired
    private AndonService andonService;

    @Autowired
    private DataMapper dataMapper;

    public WorkShopVo queryInfo(Map<String,String> params){
        WorkShopVo workShopVo=new WorkShopVo();
        //获取车间info
        WorkShop workShop=dataMapper.getWorkShopBoByStationBo(params.get("stationBo"));
        CallTypeDTO callTypeDTO=new CallTypeDTO();
        callTypeDTO.setWorkShopBo(workShop.getBo());
        callTypeDTO.setAndonTypeName(params.get("andonTypeName"));

        workShopVo.setWorkShopBo(workShop.getBo());
        workShopVo.setWorkShopDesc(workShop.getWorkShopDesc());
        return workShopVo;
    }
}
