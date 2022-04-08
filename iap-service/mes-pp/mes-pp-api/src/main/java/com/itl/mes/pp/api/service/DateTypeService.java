package com.itl.mes.pp.api.service;


import com.itl.mes.pp.api.dto.scheduleplan.DateTypeQueryDTO;
import com.itl.mes.pp.api.dto.scheduleplan.DateTypeSaveDTO;
import com.itl.mes.pp.api.dto.scheduleplan.DateTypeUpdateDTO;
import com.itl.mes.pp.api.entity.scheduleplan.DateTypeEntity;

import java.util.List;

public interface DateTypeService {


    void batchSave(DateTypeSaveDTO dateTypeSaveDTO);


    List<DateTypeEntity> findList(DateTypeQueryDTO dateTypeQueryDTO);


    void update(List<DateTypeUpdateDTO> dateTypeUpdateDTOs);

}
