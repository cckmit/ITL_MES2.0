package com.itl.mes.core.api.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.CustomData;
import com.itl.mes.core.api.vo.CustomDataVo;
import com.itl.mes.core.api.vo.CustomFullDataVo;

import java.util.List;


/**
 * <p>
 * 自定义数据表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
public interface CustomDataService extends IService<CustomData> {

    List<CustomData> selectList();

    /**
     * 查询自定义数据类型维护的数据
     *
     * @param site
     * @param customDataType
     * @return
     */
    List<CustomData> getCustomDataListByCustomDataType(String site, String customDataType);

    /**
     * 自定义数据维护 保存功能 包含 新增和更新逻辑
     *
     * @param customFullDataVo customFullDataVo
     */
    void saveCustomData(CustomFullDataVo customFullDataVo) throws CommonException;

    /**
     * 通过数据类型查询类型对应的自定义数据
     *
     * @param customDataType 数据类型
     * @return
     */
    List<CustomDataVo> selectCustomDataVoListByDataType(String customDataType);

    /**
     * 删除自定义数据类型自定义数据
     *
     * @param customDataType 自定义数据类型
     */
    void deleteCustomData(String customDataType) throws CommonException;
}