package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.pp.api.dto.schedule.ReceiveRespDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveQueryDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveRespDTO;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleReceiveEntity;
import com.itl.mes.pp.api.service.ScheduleReceiveService;
import com.itl.mes.pp.provider.config.Constant;
import com.itl.mes.pp.provider.mapper.ScheduleMapper;
import com.itl.mes.pp.provider.mapper.ScheduleReceiveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/16 15:12
 */
@Service
public class ScheduleReceiveServiceImpl implements ScheduleReceiveService {


    @Autowired
    ScheduleReceiveMapper scheduleReceiveMapper;

    @Autowired
    ScheduleMapper scheduleMapper;



    @Override
    public IPage<ReceiveRespDTO> receiveList(ScheduleReceiveQueryDTO scheduleReceiveQueryDTO) {

        if(ObjectUtil.isEmpty(scheduleReceiveQueryDTO.getPage())){
            scheduleReceiveQueryDTO.setPage(new Page(0, 10));
        }

        return scheduleReceiveMapper.receiveList(scheduleReceiveQueryDTO.getPage(),scheduleReceiveQueryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispatch(ScheduleReceiveDTO scheduleReceiveDTO) throws Exception {


        ScheduleReceiveEntity scheduleReceiveEntity = scheduleReceiveMapper.selectById(scheduleReceiveDTO.getBo());
        //当接收数量为0时，则该数据还未接收
        if(scheduleReceiveEntity.getReceiveQty().equals(new BigDecimal(0))){
            if(scheduleReceiveDTO.getReceiveQty().compareTo(scheduleReceiveEntity.getScheduleQty()) == 1){
                throw new Exception("接收数量不可大于排程数量");
            }else {
                ScheduleReceiveEntity updateDTO = new ScheduleReceiveEntity();
                updateDTO.setBo(scheduleReceiveDTO.getBo());
                updateDTO.setReceiveQty(scheduleReceiveDTO.getReceiveQty());
                updateDTO.setReceiveDate(new Date());
                updateDTO.setModifyDate(new Date());
                if(scheduleReceiveDTO.getReceiveQty().compareTo(scheduleReceiveEntity.getScheduleQty())==0){
                    updateDTO.setState(Constant.ScheduleReceiveState.RECEIVE.getValue());
                }
                updateDTO.setModifyUser(UserUtils.getCurrentUser().getUserName());
                scheduleReceiveMapper.updateById(updateDTO);
                QueryWrapper<ScheduleEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("bo",scheduleReceiveEntity.getScheduleBo());
                queryWrapper.eq("state","2");
                Integer count = scheduleMapper.selectCount(queryWrapper);
                if(count == 1){
                    ScheduleEntity scheduleEntity = new ScheduleEntity();
                    scheduleEntity.setBo(scheduleReceiveEntity.getScheduleBo());
                    scheduleEntity.setState(Constant.ScheduleState.RECEIVE.getValue());
                    scheduleEntity.setModifyDate(new Date());
                    scheduleMapper.updateById(scheduleEntity);
                }
            }

        }else {
            if(scheduleReceiveDTO.getReceiveQty().add(scheduleReceiveEntity.getReceiveQty()).compareTo(scheduleReceiveEntity.getScheduleQty())==1){
                throw new Exception("接收数量不可大于排程数量");
            }else {
                ScheduleReceiveEntity updateDTO = new ScheduleReceiveEntity();
                updateDTO.setBo(scheduleReceiveDTO.getBo());
                updateDTO.setReceiveQty(scheduleReceiveDTO.getReceiveQty().add(scheduleReceiveEntity.getReceiveQty()));
                updateDTO.setReceiveDate(new Date());
                updateDTO.setModifyDate(new Date());
                updateDTO.setModifyUser(UserUtils.getCurrentUser().getUserName());
                if(scheduleReceiveDTO.getReceiveQty().add(scheduleReceiveEntity.getReceiveQty()).compareTo(scheduleReceiveEntity.getScheduleQty())==0){
                    updateDTO.setState(Constant.ScheduleReceiveState.RECEIVE.getValue());
                }
                scheduleReceiveMapper.updateById(updateDTO);
            }

        }




    }

    @Override
    public IPage<ScheduleReceiveRespDTO> list(ScheduleReceiveQueryDTO scheduleReceiveQueryDTO) {


        if(ObjectUtil.isEmpty(scheduleReceiveQueryDTO.getPage())){
            scheduleReceiveQueryDTO.setPage(new Page(0, 10));
        }
        return scheduleReceiveMapper.scheduleReceiveList(scheduleReceiveQueryDTO.getPage(),scheduleReceiveQueryDTO);
    }


}
