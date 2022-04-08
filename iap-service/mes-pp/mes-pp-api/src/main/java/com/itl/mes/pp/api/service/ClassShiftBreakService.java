package com.itl.mes.pp.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.pp.api.dto.scheduleplan.ClassShiftBreakQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassShiftBreakEntity;


import java.util.List;

public interface ClassShiftBreakService {


    IPage<ClassShiftBreakEntity> findList(ClassShiftBreakQueryDTO classShiftBreakQueryDTO);


    void save(ClassShiftBreakEntity classShiftBreakEntity);


    void delete(List<String> ids);


    ClassShiftBreakEntity findById(String id);



}
