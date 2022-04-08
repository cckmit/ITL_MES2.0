package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.ScadaStateEntity;

import java.util.Map;

/**
 * 3号转轴车间scada状态看板服务
 */
public interface ScadaStateDashboardService extends IService<ScadaStateEntity> {

    Map<String,String> getThreeFactoryScadaState() throws CommonException;
}
