package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.CustomDataVal;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 自定义数据的值 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
@Repository
public interface CustomDataValMapper extends BaseMapper<CustomDataVal> {


    List<CustomDataAndValVo> selectCustomDataAndValByBoAndDataType(@Param("site") String site, @Param("bo") String bo, @Param("dataType") String dataType);
    List<CustomDataAndValVo> selectCustomDataAndValByBoAndDataTypeFast(@Param("site") String site, @Param("bo") String bo, @Param("dataType") String dataType);

    List<CustomDataAndValVo> selectOnlyCustomData(@Param("site") String site, @Param("bo") String bo, @Param("dataType") String dataType);
    CustomDataAndValVo selectOperationInspectionNotRequire(@Param("site") String site, @Param("bo") String bo, @Param("dataType") String dataType);
}