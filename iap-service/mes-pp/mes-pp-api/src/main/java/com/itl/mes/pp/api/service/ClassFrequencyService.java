package com.itl.mes.pp.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.pp.api.dto.scheduleplan.ClassFrequencyQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassFrequencyEntity;


import java.util.List;

public interface ClassFrequencyService {


    IPage<ClassFrequencyEntity> findList(ClassFrequencyQueryDTO classFrequencyQueryDTO);


    void save(ClassFrequencyEntity classFrequencyEntity);


    void delete(List<String> ids);


    ClassFrequencyEntity findById(String id);

}
