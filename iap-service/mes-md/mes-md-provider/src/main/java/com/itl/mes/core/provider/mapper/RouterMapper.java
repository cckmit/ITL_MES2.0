package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.Router;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 工艺路线 Mapper
 * </p>
 *
 * @author linjl
 * @since 2021-01-28
 */
public interface RouterMapper extends BaseMapper<Router> {

    //根据工艺路线编号，更新版本
    void updateByRouter(@Param("router") String router);

    // 工序工单存在该工艺路线的个数
    int isExistRouter(@Param("router") String router,@Param("version")String version);

    Router selectRouterBySfc(@Param("sfc") String sfc);

}
