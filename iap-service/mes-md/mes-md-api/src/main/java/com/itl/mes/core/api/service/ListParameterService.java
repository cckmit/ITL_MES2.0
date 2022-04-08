package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.ListParameter;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author space
 * @since 2019-06-03
 */
public interface ListParameterService extends IService<ListParameter> {

    List<ListParameter> selectList();
}