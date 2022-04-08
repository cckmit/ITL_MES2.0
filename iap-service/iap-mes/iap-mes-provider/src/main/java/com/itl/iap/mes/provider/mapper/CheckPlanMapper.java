package com.itl.iap.mes.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.CheckExecute;
import com.itl.iap.mes.api.entity.CheckPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * @time 2018年11月14日
 * @since JDK 1.8
 */

public interface CheckPlanMapper extends BaseMapper<CheckPlan> {

    IPage<CheckPlan> findList(Page page, @Param("checkPlan") CheckPlan checkPlan);
    IPage<CheckPlan> findListByState(Page page, @Param("checkPlan") CheckPlan checkPlan);

    CheckPlan selectByDeviceType(@Param("deviceType") String deviceType);
}
