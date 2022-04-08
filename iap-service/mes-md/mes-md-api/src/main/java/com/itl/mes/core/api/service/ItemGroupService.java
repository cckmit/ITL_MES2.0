package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.ItemGroupHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ItemGroup;
import com.itl.mes.core.api.vo.ItemGroupVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 物料组表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
public interface ItemGroupService extends IService<ItemGroup> {

    List<ItemGroup> selectList();

    /**
     * 通过ItemGroupHandleBO查询物料组
     *
     * @param itemGroupHandleBO itemGroupHandleBO
     * @return ItemGroup
     * @throws CommonException
     */
    ItemGroup getItemGroupByItemGroupBO(ItemGroupHandleBO itemGroupHandleBO)throws CommonException;

    /**
     * 查询物料组相关数据
     *
     * @param itemGroup 物料组
     * @return ItemGroupVo
     * @throws CommonException
     */
    ItemGroupVo getItemGroupVoByGroup(String itemGroup)throws CommonException;

    /**
     * 物料组保存
     *
     * @param itemGroupVo itemGroupVo
     * @throws CommonException
     */
    void saveItemGroup(ItemGroupVo itemGroupVo) throws CommonException;

    /**
     * 通过ItemHandleBO查询物料可分配物料组
     *
     * @param itemHandleBO itemHandleBO
     * @return List<String>
     */
    List<String> getAvailableItemGroupListByItemHandleBO(ItemHandleBO itemHandleBO);

    /**
     * 通过ItemHandleBO查询物料已分配物料组
     *
     * @param itemHandleBO itemHandleBO
     * @return List<String>
     */
    List<String> getAssignedItemGroupListBySiteAndItemBO(ItemHandleBO itemHandleBO);

    /**
     * 验证物料组是否被使用 true/false
     *
     * @param itemGroupHandleBO itemGroupHandleBO
     * @throws CommonException
     */
    boolean validateItemGroupIsUsed(ItemGroupHandleBO itemGroupHandleBO)throws CommonException;

    /**
     * 删除物料组
     *
     * @param itemGroup 物料组
     * @param modifyDate 修改时间
     * @throws CommonException
     */
    void deleteItemGroup(String itemGroup, Date modifyDate)throws CommonException;

    /**
     * 通过物料组BO查询对应的物料
     *
     * @param itemGroupBo 物料组bo
     * @return List<Item>
     * @throws CommonException
     */
    List<Item> selectItemsByItemGroupBO(String itemGroupBo)throws CommonException;

    /**
     * 通过工厂查询物料组字符串list
     *
     * @return List<String>
     * @throws CommonException
     */
    List<String> selectItemGroupListBySite()throws CommonException;
}