package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.Station;
import com.itl.mes.core.api.entity.StationTypeItem;
import com.itl.mes.core.api.service.StationService;
import com.itl.mes.core.api.service.StationTypeItemService;
import com.itl.mes.core.api.vo.StationTypeItemVo;
import com.itl.mes.core.provider.mapper.StationTypeItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 工位类型明细表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-29
 */
@Service
@Transactional
public class StationTypeItemServiceImpl extends ServiceImpl<StationTypeItemMapper, StationTypeItem> implements StationTypeItemService {


    @Autowired
    private StationTypeItemMapper stationTypeItemMapper;
    @Autowired
    private StationService stationService;

    @Override
    public List<StationTypeItem> selectList() {
        QueryWrapper<StationTypeItem> entityWrapper = new QueryWrapper<StationTypeItem>();
        //getEntityWrapper(entityWrapper, stationTypeItem);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(String stationTypeBO, List<StationTypeItemVo> assignedStationTypeItemVos) throws CommonException {
           delete(stationTypeBO);
           if(assignedStationTypeItemVos!=null){
               for(StationTypeItemVo stationTypeItemVo:assignedStationTypeItemVos){
                   Station station = stationService.selectByStation(stationTypeItemVo.getStation());
                   StationTypeItem stationTypeItem = new StationTypeItem();
                   stationTypeItem.setStationTypeBo(stationTypeBO);
                   stationTypeItem.setStationBo(station.getBo());
                   stationTypeItemMapper.insert(stationTypeItem);
               }
           }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void delete(String stationTypeBO) {
        QueryWrapper<StationTypeItem> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(StationTypeItem.STATION_TYPE_BO,stationTypeBO);
        stationTypeItemMapper.delete(entityWrapper);
    }


}