package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.ItemGroupMember;
import com.itl.mes.core.api.vo.ItemNameDescVo;

import java.util.List;

/**
 * <p>
 * 物料组成员表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
public interface ItemGroupMemberService extends IService<ItemGroupMember> {

    List<ItemGroupMember> selectList();

    /**
     * 保存物料组和物料的关系
     *
     * @param site 工厂
     * @param groupBO 物料组bo
     * @param itemList 物料list
     * @throws CommonException
     */
    void saveItemGroupItem(String site, String groupBO, List<ItemNameDescVo> itemList)throws CommonException;

    /**
     * 保存物料跟物料组的关系
     *
     * @param site 工厂
     * @param itemBo 物料bo
     * @param groupList 物料组list
     * @throws CommonException
     */
    void saveItemAndItemGroups(String site, String itemBo, List<String> groupList) throws CommonException;
}