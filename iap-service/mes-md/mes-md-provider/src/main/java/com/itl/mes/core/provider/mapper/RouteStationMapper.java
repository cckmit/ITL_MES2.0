package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.RouteStation;

/**
 * <p>
 * 工艺路线工步配置 Mapper
 * </p>
 *
 * @author pwy
 * @since 2021-3-12
 */
public interface RouteStationMapper extends BaseMapper<RouteStation> {


    int updateEffective(String bo,String effective);

    String queryRouterOperation(String router, String version);
}
