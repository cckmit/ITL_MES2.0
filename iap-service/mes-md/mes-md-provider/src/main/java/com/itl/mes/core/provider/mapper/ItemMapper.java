package com.itl.mes.core.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 物料表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@Repository
public interface ItemMapper extends BaseMapper<Item> {
    Item selectOperationOrderItem(@Param("operationOrder") String operationOrder);

    String selectProcessInfoByItem(@Param("routerBo") String routerBo);
}