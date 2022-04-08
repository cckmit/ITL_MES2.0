package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.RouteStationDTO;
import com.itl.mes.core.api.entity.RouteStation;
import com.itl.mes.core.api.entity.WorkStation;

import java.util.List;

/**
 * <p>
 * 工艺路线工步维护
 * </p>
 *
 * @author pwy
 * @since 2021-3-12
 */
public interface RouteStationService extends IService<RouteStation> {

    int updateEffective(String bo,String effective);

    String queryRouterOperation(String router, String version);

    IPage<WorkStation> selectWsByRouterAndVersion(String router, String version,Integer page,Integer pageSize);

    void updateRouteStation(List<RouteStationDTO> routeStationDTO);

    ResponseData<String> insertOrUpdate(String router,String version) throws CommonException ;
}
