package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.CustomData;
import com.itl.mes.core.api.vo.CustomDataVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 自定义数据表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
@Repository
public interface CustomDataMapper extends BaseMapper<CustomData> {

    /**
     * 通过数据类型查询类型对应的自定义数据
     *
     * @param customDataType
     * @param site
     * @return
     */
    List<CustomDataVo> selectCustomDataVoListByDataType(@Param("site") String site, @Param("customDataType") String customDataType);
    List<CustomDataVo> selectCustomDataVoListByDataTypeFast(@Param("site") String site, @Param("customDataType") String customDataType);

}