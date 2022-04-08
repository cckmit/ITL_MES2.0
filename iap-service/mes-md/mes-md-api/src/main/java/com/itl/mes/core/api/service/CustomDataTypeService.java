package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.CustomDataType;

import java.util.List;


/**
 * <p>
 * 自定义数据类型 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-29
 */
public interface CustomDataTypeService extends IService<CustomDataType> {

    List<CustomDataType> selectList();
}