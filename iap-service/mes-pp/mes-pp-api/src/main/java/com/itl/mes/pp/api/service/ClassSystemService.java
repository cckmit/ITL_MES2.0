package com.itl.mes.pp.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.pp.api.dto.scheduleplan.ClassSystemQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassSystemEntity;

import java.util.List;

public interface ClassSystemService {



    IPage<ClassSystemEntity> findList(ClassSystemQueryDTO classSystemQueryDTO);


    void save(ClassSystemEntity classSystemEntity);


    void delete(List<String> ids);


    ClassSystemEntity findById(String id);

}
