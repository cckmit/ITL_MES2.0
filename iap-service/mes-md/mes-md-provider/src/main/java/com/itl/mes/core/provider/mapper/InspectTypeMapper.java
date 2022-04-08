package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.entity.InspectType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检验类型维护 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2019-08-28
 */
@Repository
public interface InspectTypeMapper extends BaseMapper<InspectType> {
    /**
     * 分页查询检验类型数据
     * @param page
     * @param params
     * @return
     */
    List<Map> selectPageInspectTypeList(IPage<Map> page, @Param("params") Map<String, Object> params);
    /**
     * 查询检验类型数据
     * @param params
     * @return
     */
    List<Map> selectInspectTypeList(@Param("params") Map<String, Object> params);
    List<Map> selectPageInspectTypeListByState(IPage<Map> page, @Param("params") Map<String, Object> params);

}
