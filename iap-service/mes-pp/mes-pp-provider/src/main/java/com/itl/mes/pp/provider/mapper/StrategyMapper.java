package com.itl.mes.pp.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.pp.api.entity.Strategy;
import com.itl.mes.pp.api.entity.WorkOrderBinding;

import java.util.List;
import java.util.Map;

/**
 * @author xuejh
 * @date 2020/11/12 9:52
 */
public interface StrategyMapper extends BaseMapper<Strategy> {

    List<Strategy> getByWorkshopAndLine(Strategy strategy);

}
