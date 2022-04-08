package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.CustomDataVal;
import com.itl.mes.core.api.vo.CustomDataAndValVo;

import java.util.List;

/**
 * <p>
 * 自定义数据的值 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
public interface CustomDataValService extends IService<CustomDataVal> {

    List<CustomDataVal> selectList();

    /**
     * 根据 工厂、BO、数据类型查询自定义数据和自定义数据值
     *
     * @param site 工厂
     * @param bo bo
     * @param dataType 自定义数据类型
     * @return List<CustomDataAndValVo>
     */
    List<CustomDataAndValVo> selectCustomDataAndValByBoAndDataType(String site, String bo, String dataType);

    /**
     * 查询bo指定属性的自定义值
     *
     * @param bo bo
     * @param filed 指定属性
     * @return List< CustomDataVal >
     */
    CustomDataVal getCustomDataValFieldByBo(String bo, String filed);

    /**
     *保存自定义数据值
     *
     * @param  customDataValRequest customDataValRequest
     * @throws CommonException 异常
     */
    void saveCustomDataVal(CustomDataValRequest customDataValRequest) throws CommonException;

    /**
     * 删除指定BO自定义数据值
     *
     * @param site 工厂
     * @param bo bo
     * @param customDataTypeEnum customDataTypeEnum
     */
    void deleteCustomDataValByBoAndType(String site, String bo, CustomDataTypeEnum customDataTypeEnum);

    /**
     * 删除指定BO自定义数据值
     *
     * @param site 工厂
     * @param bo bo
     * @param dataType 自定会数据类型
     */
    void deleteCustomDataValByBoAndType(String site, String bo, String dataType);

    List<CustomDataAndValVo> selectOnlyCustomData(String site, String bo, String dataType);
}