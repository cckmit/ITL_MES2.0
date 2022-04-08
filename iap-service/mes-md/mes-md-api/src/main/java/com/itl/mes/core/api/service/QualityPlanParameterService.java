package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.QualityPlanParameter;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author space
 * @since 2019-08-30
 */
public interface QualityPlanParameterService extends IService<QualityPlanParameter> {

    List<QualityPlanParameter> selectList();
}