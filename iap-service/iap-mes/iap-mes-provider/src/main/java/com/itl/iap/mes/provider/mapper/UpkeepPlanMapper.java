package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * @time 2018年11月14日
 * @since JDK 1.8
 */
@Mapper
public interface UpkeepPlanMapper extends BaseMapper<UpkeepPlan> {

    IPage<UpkeepPlan> findList(Page page, @Param("upkeepPlan") UpkeepPlan upkeepPlan);
    IPage<UpkeepPlan> findListByState(Page page, @Param("upkeepPlan") UpkeepPlan upkeepPlan);

    UpkeepPlan selectByDeviceType(@Param("deviceType") String deviceType,@Param("upkeepPlanName") String upkeepPlanName);
    List<UpkeepPlan> getByDeviceTypeWithOutupkeepPlanName(@Param("deviceType") String deviceType);
}
