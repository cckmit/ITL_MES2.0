package com.itl.mes.pp.api.service;

import com.itl.mes.pp.api.dto.CapacityLoadQueryDTO;
import com.itl.mes.pp.api.dto.CapacityLoadReportDTO;

/**
 * @auth liuchenghao
 * @date 2021/1/7
 */
public interface CapacityLoadService {


    CapacityLoadReportDTO findCapacityLoadList(CapacityLoadQueryDTO capacityLoadQueryDTO);


//    void saveCapacityLoad(String deviceBo);

}
