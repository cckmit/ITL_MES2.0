package com.itl.mes.pp.provider.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.mes.pp.api.dto.scheduleplan.DateTypeQueryDTO;
import com.itl.mes.pp.api.dto.scheduleplan.DateTypeSaveDTO;
import com.itl.mes.pp.api.dto.scheduleplan.DateTypeUpdateDTO;
import com.itl.mes.pp.api.entity.scheduleplan.DateTypeEntity;
import com.itl.mes.pp.api.service.DateTypeService;
import com.itl.mes.pp.provider.mapper.DateTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * @User liuchenghao
 * @Date 2020/11/25 10:15
 */
@Service
public class DateTypeServiceImpl implements DateTypeService {

    @Autowired
    DateTypeMapper dateTypeMapper;



    @Override
    public void batchSave(DateTypeSaveDTO dateTypeSaveDTO) {

//        List<DateTypeEntity> dateTypeEntities = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dateTypeSaveDTO.getStartDate());
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dateTypeSaveDTO.getEndDate());
        while (dateTypeSaveDTO.getEndDate().after(calBegin.getTime())) {
            DateTypeEntity dateTypeEntity = new DateTypeEntity();
            dateTypeEntity.setYear(calBegin.get(Calendar.YEAR));
            dateTypeEntity.setMonth(calBegin.get(Calendar.MONTH)+1);
            dateTypeEntity.setDay(calBegin.get(Calendar.DAY_OF_MONTH));
            dateTypeEntity.setDate(calBegin.getTime());
            int week = calBegin.get(Calendar.DAY_OF_WEEK) - 1;
            //0代表周日，6代表周六
            if (week == 6 || week == 0) {
                dateTypeEntity.setState(2);
            } else {
                dateTypeEntity.setState(1);
            }
            dateTypeMapper.insert(dateTypeEntity);
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @Override
    public List<DateTypeEntity> findList(DateTypeQueryDTO dateTypeQueryDTO) {

        QueryWrapper<DateTypeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year",dateTypeQueryDTO.getYear());
        queryWrapper.eq("month",dateTypeQueryDTO.getMonth());
        queryWrapper.orderByAsc("day");
        List<DateTypeEntity> dateTypeEntities = dateTypeMapper.selectList(queryWrapper);

        return dateTypeEntities;
    }

    @Override
    public void update(List<DateTypeUpdateDTO> dateTypeUpdateDTOs) {

        dateTypeUpdateDTOs.forEach(dateTypeUpdateDTO -> {
            DateTypeEntity dateTypeEntity = new DateTypeEntity();
            BeanUtil.copyProperties(dateTypeUpdateDTO,dateTypeEntity);
            dateTypeMapper.updateById(dateTypeEntity);
        });

    }
}
