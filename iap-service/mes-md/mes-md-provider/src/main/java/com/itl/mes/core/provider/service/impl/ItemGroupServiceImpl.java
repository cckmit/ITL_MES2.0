package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.ItemGroupHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ItemGroup;
import com.itl.mes.core.api.entity.ItemGroupMember;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.ItemGroupMemberService;
import com.itl.mes.core.api.service.ItemGroupService;
import com.itl.mes.core.api.vo.ItemGroupVo;
import com.itl.mes.core.api.vo.ItemNameDescVo;
import com.itl.mes.core.provider.mapper.ItemGroupMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 物料组表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@Service
@Transactional
public class ItemGroupServiceImpl extends ServiceImpl<ItemGroupMapper, ItemGroup> implements ItemGroupService {


    @Autowired
    private ItemGroupMapper itemGroupMapper;

    @Autowired
    private ItemGroupMemberService itemGroupMemberService;

    @Autowired
    private CustomDataValService customDataValService;

    @Resource
    private UserUtil userUtil;

    @Override
    public List<ItemGroup> selectList() {
        QueryWrapper<ItemGroup> entityWrapper = new QueryWrapper<ItemGroup>();
        //getEntityWrapper(entityWrapper, itemGroup);
        return super.list(entityWrapper);
    }


    /**
     * 通过ItemGroupHandleBO查询物料组
     *
     * @param itemGroupHandleBO
     * @return
     * @throws CommonException
     */
    @Override
    public ItemGroup getItemGroupByItemGroupBO(ItemGroupHandleBO itemGroupHandleBO)throws CommonException {

        ItemGroup itemGroup = super.getById( itemGroupHandleBO.getBo() );
        if( itemGroup==null ){
            throw new CommonException( "物料组"+itemGroupHandleBO.getItemGroup()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return itemGroup;
    }

    /**
     * 查询物料组相关数据
     *
     * @param itemGroup
     * @return
     * @throws CommonException
     */
    @Override
    public ItemGroupVo getItemGroupVoByGroup(String itemGroup )throws CommonException{

        ItemGroupHandleBO itemGroupHandleBO = new ItemGroupHandleBO( UserUtils.getSite(), itemGroup );
        ItemGroup itemGroupEntity = getItemGroupByItemGroupBO( itemGroupHandleBO );
        ItemGroupVo itemGroupVo = new ItemGroupVo();
        BeanUtils.copyProperties( itemGroupEntity,itemGroupVo );
        List<Item> itemList = selectItemsByItemGroupBO( itemGroupHandleBO.getBo() );
        if( itemList.size() > 0 ){
            List<ItemNameDescVo> itemNameDescVoList = new ArrayList<>();
            ItemNameDescVo itemNameDescVo = null;
            for( Item item:itemList ){
                itemNameDescVo = new ItemNameDescVo();
                BeanUtils.copyProperties( item,itemNameDescVo );
                itemNameDescVoList.add( itemNameDescVo );
            }
            itemGroupVo.setAssignedItemList( itemNameDescVoList );
        }
        itemGroupVo.setCustomDataAndValVoList( customDataValService
                .selectCustomDataAndValByBoAndDataType( itemGroupHandleBO.getSite(), itemGroupHandleBO.getBo(), CustomDataTypeEnum.ITEM_GROUP.getDataType() ) );
        return itemGroupVo;
    }

    /**
     * 物料组保存
     *
     * @param itemGroupVo
     * @throws CommonException
     */
    @Override
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class} )
    public void saveItemGroup(ItemGroupVo itemGroupVo) throws CommonException {

        String site = UserUtils.getSite();
        String username = userUtil.getUser().getUserName();

        ItemGroupHandleBO itemGroupHandleBO = new ItemGroupHandleBO( site,itemGroupVo.getItemGroup() );
        ItemGroup itemGroup = super.getById( itemGroupHandleBO.getBo() );
        if( itemGroup==null ){ //代表新增

            Date createDate = new Date();
            itemGroup = new ItemGroup();
            itemGroup.setBo( itemGroupHandleBO.getBo() );
            itemGroup.setSite( site );
            itemGroup.setItemGroup( itemGroupVo.getItemGroup() );
            itemGroup.setGroupName( itemGroupVo.getGroupName() );
            itemGroup.setGroupDesc( itemGroupVo.getGroupDesc() );
            itemGroup.setObjectSetBasicAttribute( username, new Date() );
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean( itemGroup );
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION );
            }
            itemGroup.setCreateDate(createDate);
            super.save( itemGroup ); //新增

        }else{ //代表更新

            Date frontModifyDate = itemGroupVo.getModifyDate(); //前台传递的时间值
            Date modifyDate = itemGroup.getModifyDate(); //后台数据库的值
            CommonUtil.compareDateSame( frontModifyDate, modifyDate ); //比较时间是否相等

            ItemGroup itemGroupEntity = new ItemGroup();
            Date newModifyDate = new Date();

            itemGroupEntity.setBo( itemGroupHandleBO.getBo() );
            itemGroupEntity.setItemGroup( itemGroupVo.getItemGroup() );
            itemGroupEntity.setGroupName( itemGroupVo.getGroupName() );
            itemGroupEntity.setGroupDesc( itemGroupVo.getGroupDesc() );
            itemGroupEntity.setModifyUser( username );
            itemGroupEntity.setModifyDate( newModifyDate );
            super.updateById( itemGroupEntity ); //更新
        }
        //保存分配物料
        if( itemGroupVo.getAssignedItemList()!=null ){
            itemGroupMemberService.saveItemGroupItem( site, itemGroupHandleBO.getBo(), itemGroupVo.getAssignedItemList() );
        }
        //保存自定义数据
        if( itemGroupVo.getCustomDataValVoList()!=null ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( itemGroupHandleBO.getBo() );
            customDataValRequest.setSite( site );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.ITEM_GROUP.getDataType() );
            customDataValRequest.setCustomDataValVoList( itemGroupVo.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }

    }

    /**
     * 通过ItemHandleBO查询物料可分配物料组
     *
     * @param itemHandleBO
     * @return
     */
    @Override
    public List<String> getAvailableItemGroupListByItemHandleBO( ItemHandleBO itemHandleBO ){

        return itemGroupMapper.getAvailableItemGroupListBySiteAndItemBO( itemHandleBO.getSite(), itemHandleBO.getBo() );

    }

    /**
     * 通过ItemHandleBO查询物料已分配物料组
     *
     * @param itemHandleBO
     * @return
     */
    @Override
    public List<String> getAssignedItemGroupListBySiteAndItemBO( ItemHandleBO itemHandleBO ){

        return itemGroupMapper.getAssignedItemGroupListBySiteAndItemBO( itemHandleBO.getSite(), itemHandleBO.getBo() );

    }

    /**
     * 验证物料组是否被使用
     *
     * @param itemGroupHandleBO
     * @throws CommonException
     */
    @Override
    public boolean validateItemGroupIsUsed(ItemGroupHandleBO itemGroupHandleBO )throws CommonException{
        QueryWrapper<ItemGroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq( ItemGroupMember.ITEM_GROUP_BO, itemGroupHandleBO.getBo() );
        int count = itemGroupMemberService.count( wrapper );
        if( count >0 ){
            return true;
        }
        return false;
    }

    /**
     * 删除物料组
     *
     * @param itemGroup
     * @param modifyDate
     */
    @Override
    public void deleteItemGroup( String itemGroup, Date modifyDate ) throws CommonException {

        ItemGroupHandleBO itemGroupHandleBO = new ItemGroupHandleBO( UserUtils.getSite(), itemGroup );
        ItemGroup itemGroupEntity = getItemGroupByItemGroupBO( itemGroupHandleBO );
        CommonUtil.compareDateSame( modifyDate,itemGroupEntity.getModifyDate() );
        //验证物料组是否被使用
        if( validateItemGroupIsUsed( itemGroupHandleBO ) ){
            throw new CommonException( "物料组"+itemGroupHandleBO.getItemGroup()+"已被使用，不能删除", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        QueryWrapper<ItemGroup> delWrapper = new QueryWrapper<>();
        delWrapper.eq( ItemGroup.BO, itemGroupEntity.getBo() ).eq( ItemGroup.MODIFY_DATE, modifyDate );
        //删除物料组
        Integer integer = itemGroupMapper.delete( delWrapper );
        if( integer==0 ){
            throw new CommonException( "数据已修改，请重新查询再执行删除操作", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        //删除物料组自定义数据值
        customDataValService.deleteCustomDataValByBoAndType( itemGroupHandleBO.getSite(), itemGroupHandleBO.getBo(), CustomDataTypeEnum.ITEM_GROUP );

    }

    /**
     * 通过物料组BO查询对应的物料
     *
     * @param itemGroupBo
     * @return
     * @throws CommonException
     */
    @Override
    public List<Item> selectItemsByItemGroupBO( String itemGroupBo ) throws CommonException {

        return itemGroupMapper.selectItemByItemGroupBO( itemGroupBo );
    }

    /**
     * 通过工厂查询物料组字符串list
     *
     * @return
     */
    @Override
    public List<String> selectItemGroupListBySite() throws CommonException {
        return itemGroupMapper.selectItemGroupListBySite( UserUtils.getSite() );
    }
}