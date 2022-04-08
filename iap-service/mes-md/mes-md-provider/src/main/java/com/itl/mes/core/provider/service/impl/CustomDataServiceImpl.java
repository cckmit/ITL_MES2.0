package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.CustomDataHandleBO;
import com.itl.mes.core.api.entity.CustomData;
import com.itl.mes.core.api.entity.CustomDataVal;
import com.itl.mes.core.api.service.CustomDataService;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.vo.CustomDataVo;
import com.itl.mes.core.api.vo.CustomFullDataVo;
import com.itl.mes.core.provider.mapper.CustomDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义数据表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
@Service
@Transactional
public class CustomDataServiceImpl extends ServiceImpl<CustomDataMapper, CustomData> implements CustomDataService {


    @Autowired
    private CustomDataMapper customDataMapper;

    @Autowired
    private CustomDataValService customDataValService;

    @Override
    public List<CustomData> selectList() {
        QueryWrapper<CustomData> entityWrapper = new QueryWrapper<CustomData>();
        //getEntityWrapper(entityWrapper, customData);
        return super.list(entityWrapper);
    }


    /**
     * 通过数据类型查询类型对应的自定义数据
     *
     * @param customDataType 数据类型
     * @return List<CustomDataVo>
     */
    @Override
    public List<CustomDataVo> selectCustomDataVoListByDataType(String customDataType ) {
        List<CustomDataVo> customDataVos = customDataMapper.selectCustomDataVoListByDataType(UserUtils.getSite(), customDataType);
        return customDataVos;
    }


    /**
     * 查询自定义数据类型维护的数据
     *
     * @param site 工厂
     * @param customDataType 自定义数据类型
     * @return List<CustomData>
     */
    @Override
    public List<CustomData> getCustomDataListByCustomDataType( String site, String customDataType ){

        QueryWrapper<CustomData> wrapperEntity = new QueryWrapper<>();
        CustomData customData = new CustomData();
        customData.setCustomDataType( customDataType );
        customData.setSite( site );
        wrapperEntity.setEntity( customData );
        return customDataMapper.selectList( wrapperEntity );
    }


    /**
     * 自定义数据维护 保存功能 包含 新增和更新逻辑
     *
     * @param customFullDataVo customDataVoList
     */
    @Override
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class} )
    public void saveCustomData( CustomFullDataVo customFullDataVo ) throws CommonException {

        String site = UserUtils.getSite();

        //查询数据类型是否维护
        List<CustomData> customDataList = getCustomDataListByCustomDataType( site, customFullDataVo.getCustomDataType() );

        if( customDataList.size()==0 ){ //未维护 新增

            insertCustomData( site, customFullDataVo.getCustomDataVoList() ); //新增

        }else{ //已维护 更新

            updateCustomData( site, customDataList, customFullDataVo.getCustomDataVoList() );

        }

    }


    /**
     * 自定义数据维护 新增功能
     *
     * @param site 工厂
     * @param customDataVoList customDataVoList
     * @throws CommonException 异常
     */
    private void insertCustomData( String site,List<CustomDataVo> customDataVoList ) throws CommonException {

        Date createDate = new Date();
        CustomData customData = null;
        CustomDataVo customDataVo = null;
        List< CustomData > customDataList = new ArrayList<>();
        ValidationUtil.ValidResult validResult = null;
        for( int i=0,len=customDataVoList.size(); i<len; i++ ){

            customData = new CustomData();
            customDataVo = customDataVoList.get( i );
            customData.setBo( new CustomDataHandleBO( site, customDataVo.getCustomDataType(), customDataVo.getCdField() ).getBo() );
            customData.setSite( site );
            customData.setSequence( customDataVo.getSequence() );
            customData.setCustomDataType( customDataVo.getCustomDataType() );
            customData.setCdField( customDataVo.getCdField() );
            customData.setCdLabel( StrUtil.isBlank( customDataVo.getCdLabel() )?customDataVo.getCdField():customDataVo.getCdLabel() );
            customData.setIsDataRequired( customDataVo.getIsDataRequired() );
            customData.setObjectDate( createDate );

            validResult = ValidationUtil.validateBean( customData ); //验证数据是否合规
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            customDataList.add( customData );

        }
        if( customDataList.size() > 0 ){
            super.saveBatch( customDataList ); //批量保存
        }

    }

    /**
     * 自定义数据维护 更新功能
     *
     * @param site 工厂
     * @param customDataList customDataList
     * @param customDataVoList customDataVoList
     * @throws CommonException 异常
     */
    private void updateCustomData( String site, List<CustomData> customDataList, List< CustomDataVo > customDataVoList ) throws CommonException {

        HashMap< String,CustomData > customDataMap = new HashMap<>(); //用来存放已有的自定义数据
        HashMap< String,CustomDataVo > customDataVoMap = new HashMap<>(); //用来存放页面接受的自定义数据

        for( CustomData customData:customDataList ){ //已存在的自定义数据
            customDataMap.put( customData.getCdField(), customData );
        }
        for( CustomDataVo customDataVo:customDataVoList ){ //接收页面的自定义数据
            customDataVoMap.put( customDataVo.getCdField(), customDataVo );
        }

        HashMap< String,String > insertMap = new HashMap<>(); //新增自定义数据
        HashMap< String,String > updateMap = new HashMap<>(); //更新自定义数据

        for( String key:customDataVoMap.keySet() ){ //遍历接受数据
            if( customDataMap.containsKey( key ) ){ //代表是更新,包含被使用的数据
                updateMap.put( key, key );
            }else{ //代表新增
                insertMap.put( key, key );
            }
        }

        HashMap< String,String > deleteMap = new HashMap<>();
        //计算已有数据和需要更新的数据的差集，可得出需要删除的已有的数据
        for( String key:customDataMap.keySet() ){
            if( !updateMap.containsKey( key ) ){ //需要删除的自定义数据
                deleteMap.put( key,key );
            }
        }

        Date creteOrUpdate = new Date();
        //更新数据，需要判断数据是否已使用，已使用的数据不能更新
        List< String > updateBoList = new ArrayList<>();
        for( String key:updateMap.keySet() ){
            updateBoList.add( customDataMap.get( key ).getBo() );
        }
        if( updateBoList.size() > 0 ){
            //查询数据是否有被使用的数据
            List<CustomDataVal> customDataValList = getUsedCustomDataValListByCustomDataBOs( updateBoList );
            if( customDataValList.size() > 0 ){ //代表数据已被使用，不能更新，去除掉updateMap不能更新的数据
                for( CustomDataVal customDataVal: customDataValList ){
                    updateMap.remove( customDataVal.getAttribute() ); //移除被使用的数据，不更新
                }
            }
        }

        ValidationUtil.ValidResult validResult = null; //验证数据
        //updateMap已经移除正被使用的key
        CustomData customData = null;
        CustomData updateCustomData = null;
        CustomDataVo customDataVo = null;
        List<CustomData> updateList = new ArrayList<>();
        for( String key:updateMap.keySet() ){
            customData = customDataMap.get( key );
            customDataVo = customDataVoMap.get( key );
            updateCustomData = new CustomData();
            updateCustomData.setBo( customData.getBo() );
            updateCustomData.setSequence( customDataVo.getSequence() );
            updateCustomData.setCdField( customDataVo.getCdField() );
            updateCustomData.setCdLabel( StrUtil.isBlank( customDataVo.getCdLabel() )?customDataVo.getCdField():customDataVo.getCdLabel() );
            updateCustomData.setIsDataRequired( customDataVo.getIsDataRequired() );
            CommonUtil.compareDateSame( customDataVo.getModifyDate(), customData.getModifyDate() ); //比较时间是否相等
            updateCustomData.setModifyDate( creteOrUpdate );

            validResult = ValidationUtil.validateBean( updateCustomData ); //验证数据是否合规
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION );
            }
            updateList.add( updateCustomData );

        }
        // 更新数据
        if( updateList.size() >0 ){ //批量更新

            super.updateBatchById( updateList );
        }

        //新增数据
        List< CustomDataVo > insertList = new ArrayList<>();
        for( String key:insertMap.keySet() ){
            validResult = ValidationUtil.validateBean( customDataVoMap.get( key ) ); //验证数据是否合规
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION );
            }
            insertList.add( customDataVoMap.get( key ) );
        }
        if( insertList.size()>0 ){
            insertCustomData( site, insertList );
        }

        //删除数据
        List<String> deleteList = new ArrayList<>();
        for( String key:deleteMap.keySet() ){
            deleteList.add( customDataMap.get( key ).getBo() );
        }
        if ( deleteList.size()>0 ){ //删除
            //通过boLis删除自定义数据,并且验证自定义字段是否能删除
            deleteAttributeByBo( deleteList );
        }

    }


    /**
     * 通过自定义数据维护表bo数组，查询该数据字段是否被使用
     *
     * @param boList boList
     * @return List<CustomDataVal>
     */
    private List<CustomDataVal> getUsedCustomDataValListByCustomDataBOs( List<String> boList ){

        QueryWrapper<CustomDataVal> entityWrapper = new QueryWrapper<>();
        List<CustomDataVal> customDataValList = new ArrayList<>();
        List<CustomDataVal> existCustomDataValList = null;
        for( String customBo:boList ){
            entityWrapper = new QueryWrapper<>();
            entityWrapper.eq( CustomDataVal.CUSTOM_DATA_BO,customBo );
            existCustomDataValList = customDataValService.list( entityWrapper );
            if( existCustomDataValList.size() > 0  ){
                customDataValList.add( existCustomDataValList.get( 0 ) );
            }
        }
        return customDataValList;
    }

    /**
     * 通过boLis删除自定义数据,并且验证自定义字段是否能删除
     *
     * @param boList boList
     * @throws CommonException 异常
     */
    private void deleteAttributeByBo( List<String> boList )throws CommonException{
        //查询自定义数据是否被使用，被使用则不能删除
        List<CustomDataVal> customDataValList = getUsedCustomDataValListByCustomDataBOs( boList );
        if ( customDataValList.size() >0 ){

            String attributes = "";
            for( int i=0; i<customDataValList.size(); i++ ){
                if( i == 0 ){
                    attributes = customDataValList.get( i ).getAttribute();
                }else {
                    attributes = attributes + "," + customDataValList.get( i ).getAttribute();
                }
            }
            throw new CommonException( "自定义数据"+attributes+"已被使用不能删除", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        super.removeByIds( boList );
    }


    /**
     * 删除自定义数据类型自定义数据
     *
     * @param customDataType 自定义数据类型
     */
    @Override
    public void deleteCustomData( String customDataType ) throws CommonException {

        List<CustomData> customDataList = getCustomDataListByCustomDataType( UserUtils.getSite(), customDataType );
        if( customDataList==null || customDataList.size()==0 ){
            throw new CommonException( "数据未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        List<String> boList = customDataList.stream().map( CustomData::getBo ).collect( Collectors.toList() );
        //删除自定义数据
        deleteAttributeByBo( boList );

    }


}