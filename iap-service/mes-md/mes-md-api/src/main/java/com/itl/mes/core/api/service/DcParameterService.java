package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.DcParameter;

import java.util.List;

/**
 * <p>
 * 数据收集组参数表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-04
 */
public interface DcParameterService extends IService<DcParameter> {

    List<DcParameter> selectList();


    DcParameter getDcParameterByParamName(String paramName, String dcGroup) throws CommonException;

    List<DcParameter> getDcParameterVoByFuzzy(String paramName) throws CommonException;
}