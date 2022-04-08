package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ItemGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 物料组表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@Repository
public interface ItemGroupMapper extends BaseMapper<ItemGroup> {

    /**
     * 查询物料可分配物料组
     *
     * @param site
     * @param itemBo
     * @return
     */
    List<String> getAvailableItemGroupListBySiteAndItemBO(@Param("site") String site, @Param("itemBo") String itemBo);

    /**
     * 查询物料已分配物料组
     *
     * @param site
     * @param itemBo
     * @return
     */
    List<String> getAssignedItemGroupListBySiteAndItemBO(@Param("site") String site, @Param("itemBo") String itemBo);

    /**
     * 查询物料组对应得物料
     *
     * @param itemGroupBo
     * @return
     */
    List<Item> selectItemByItemGroupBO(@Param("itemGroupBo") String itemGroupBo);

    /**
     * 通过工厂查询物料组字符串list
     *
     * @param site
     * @return
     */
    List<String> selectItemGroupListBySite(@Param("site") String site);

}