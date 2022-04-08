package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.RouterProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 工艺路线路线图 Mapper
 * </p>
 *
 * @author linjl
 * @since 2021-01-28
 */
public interface RouterProcessMapper extends BaseMapper<RouterProcess> {

    RouterProcess selectByRouterBo(@Param("routerBo") String routerBo);
    Map<String,String> selectByShopOrder(@Param("shopOrder") String shopOrder);
    void updateIsUsed(@Param("bo") String bo);
}
