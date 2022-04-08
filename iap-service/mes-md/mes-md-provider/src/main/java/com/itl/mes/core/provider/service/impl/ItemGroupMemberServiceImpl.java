package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.mes.core.api.bo.ItemGroupHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ItemGroup;
import com.itl.mes.core.api.entity.ItemGroupMember;
import com.itl.mes.core.api.service.ItemGroupMemberService;
import com.itl.mes.core.api.service.ItemGroupService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.vo.ItemNameDescVo;
import com.itl.mes.core.provider.mapper.ItemGroupMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 物料组成员表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@Service
@Transactional
public class ItemGroupMemberServiceImpl extends ServiceImpl<ItemGroupMemberMapper, ItemGroupMember> implements ItemGroupMemberService {

    @Autowired
    private ItemGroupMemberMapper itemGroupMemberMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemGroupService itemGroupService;

    @Override
    public List<ItemGroupMember> selectList() {
        QueryWrapper<ItemGroupMember> entityWrapper = new QueryWrapper<ItemGroupMember>();
        //getEntityWrapper(entityWrapper, itemGroupMember);
        return super.list(entityWrapper);
    }



    /**
     * 保存物料组和物料的关系
     *
     * @param site 工厂
     * @param groupBO 物料组bo
     * @param itemList 物料list
     * @throws CommonException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void saveItemGroupItem(String site, String groupBO, List<ItemNameDescVo> itemList)throws CommonException {

        //首先删除该物料组已存在的关系
        QueryWrapper<ItemGroupMember> itemGroupMemberEntityWrapper = new QueryWrapper<>();
        itemGroupMemberEntityWrapper.eq( ItemGroupMember.ITEM_GROUP_BO, groupBO );
        super.remove( itemGroupMemberEntityWrapper );

        //一个一个保存物料
        ItemNameDescVo itemNameDescVo = null;
        ItemGroupMember itemGroupMember = null;
        ItemHandleBO itemHandleBO = null;
        Item item = null;
        QueryWrapper<ItemGroupMember> groupMemberWrapper = null;
        for( int i=0,len=itemList.size(); i<itemList.size(); i++ ){

            itemNameDescVo = itemList.get( i );
            itemHandleBO = new ItemHandleBO( site,itemNameDescVo.getItem(),itemNameDescVo.getVersion() );
            item = itemService.getById( itemHandleBO.getBo() );
            if( item==null ){
                throw new CommonException( "物料"+itemNameDescVo.getItem()+"，版本"+itemNameDescVo.getVersion()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            groupMemberWrapper = new QueryWrapper<>();
            itemGroupMember = new ItemGroupMember();
            itemGroupMember.setItemBo( item.getBo() );
            itemGroupMember.setItemGroupBo( groupBO );
            groupMemberWrapper.eq( ItemGroupMember.ITEM_GROUP_BO, itemGroupMember.getItemGroupBo() )
                    .eq( ItemGroupMember.ITEM_BO, itemGroupMember.getItemBo() );
            super.save( itemGroupMember );

        }

    }

    /**
     * 保存物料跟物料组的关系
     *
     * @param site 工厂
     * @param itemBo 物料bo
     * @param groupList 物料组list
     * @throws CommonException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void saveItemAndItemGroups( String site, String itemBo, List<String> groupList ) throws CommonException {

        //首先删除物料跟物料组之间的关联
        QueryWrapper<ItemGroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq( ItemGroupMember.ITEM_BO,itemBo );
        super.remove( wrapper );

        //保存物料跟物料组之间的关系
        ItemGroupHandleBO itemGroupHandleBO = null;
        ItemGroup itemGroup = null;
        ItemGroupMember itemGroupMember = null;
        for( int i=0; i<groupList.size(); i++ ){

            itemGroupHandleBO = new ItemGroupHandleBO( site,groupList.get( i ) );
            itemGroup = itemGroupService.getById( itemGroupHandleBO.getBo() );
            if ( itemGroup==null ){
                throw new CommonException( "物料组"+ itemGroupHandleBO.getItemGroup()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
            }
            itemGroupMember = new ItemGroupMember();
            itemGroupMember.setItemGroupBo( itemGroupHandleBO.getBo() );
            itemGroupMember.setItemBo( itemBo );
            super.save( itemGroupMember );
        }

    }


}