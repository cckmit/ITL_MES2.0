package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.CustomData;
import com.itl.mes.core.api.entity.CustomDataVal;
import com.itl.mes.core.api.service.CustomDataService;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import com.itl.mes.core.provider.mapper.CustomDataValMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义数据的值 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
@Service
@Transactional
public class CustomDataValServiceImpl extends ServiceImpl<CustomDataValMapper, CustomDataVal> implements CustomDataValService {


    @Autowired
    private CustomDataValMapper customDataValMapper;

    @Autowired
    private CustomDataService customDataService;

    @Override
    public List<CustomDataVal> selectList() {
        QueryWrapper<CustomDataVal> entityWrapper = new QueryWrapper<CustomDataVal>();
        //getEntityWrapper(entityWrapper, customDataVal);
        return super.list(entityWrapper);
    }


    /**
     * 根据 工厂、BO、数据类型查询自定义数据和自定义数据值
     *
     * @param site 工厂
     * @param bo bo
     * @param dataType 数据类型
     * @return List<CustomDataAndValVo>
     */
    @Override
    public List<CustomDataAndValVo> selectCustomDataAndValByBoAndDataType(String site, String bo, String dataType ){

        return customDataValMapper.selectCustomDataAndValByBoAndDataType( site, bo, dataType );

    }


    /**
     * 查询bo自定义值
     *
     * @param bo bo
     * @return List< CustomDataVal >
     */
    public List< CustomDataVal > getCustomDataValListByBo( String bo ){

        QueryWrapper<CustomDataVal> wrapper = new QueryWrapper<>();
        wrapper.eq( CustomDataVal.BO, bo );
        return super.list( wrapper );

    }

    /**
     * 查询bo指定属性的自定义值
     *
     * @param bo bo
     * @param filed 指定属性
     * @return List< CustomDataVal >
     */
    @Override
    public CustomDataVal getCustomDataValFieldByBo( String bo ,String filed ){
        QueryWrapper<CustomDataVal> wrapper = new QueryWrapper<>();
        wrapper.eq( CustomDataVal.BO, bo ).eq( CustomDataVal.ATTRIBUTE, filed );
        List<CustomDataVal> customDataValList = super.list( wrapper );
        if( customDataValList.size() >0 ){
            return customDataValList.get( 0 );
        }
        return null;
    }


    /**
     * 自定义值list是否包含map中的key，不包含则报错
     *
     * @param requiredAttributeMap Map<String,String> 需要属性集合
     * @param customDataValList 被验证的list
     * @throws CommonException 异常
     */
    public void customDataValListIsContainsKeys( Map<String,String> requiredAttributeMap, List<CustomDataVal> customDataValList)
            throws CommonException {

        String[] keys = new String[customDataValList.size()];
        for( int i=0; i< customDataValList.size(); i++ ){
            keys[i] = customDataValList.get( i ).getAttribute();
        }
        for( String key:requiredAttributeMap.keySet() ){

            if( !ArrayUtil.contains( keys,key ) ){
                throw new CommonException( "必填属性"+key+"不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

    }


    /**
     *保存自定义数据值
     *
     * @param customDataValRequest customDataValRequest
     * @throws CommonException 异常
     */
    @Override
    public void saveCustomDataVal(CustomDataValRequest customDataValRequest) throws CommonException{

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean( customDataValRequest ); //初步验证数据合法性
        if( validResult.hasErrors() ){
            throw new CommonException(validResult.getErrors(),CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String bo = customDataValRequest.getBo();
        String site = customDataValRequest.getSite();
        Boolean doMerge = customDataValRequest.getDoMerge();
        String customDataType = customDataValRequest.getCustomDataType();
        List<CustomDataValVo> customDataValVoList = customDataValRequest.getCustomDataValVoList();

        //查询数据类型是否维护
        List<CustomData> customDataList = customDataService.getCustomDataListByCustomDataType( site, customDataType );
        if( customDataList.size()==0 ){
            throw new CommonException( "数据类型"+customDataType+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        Map<String,String> attributeBOMap = new HashMap<>();//存在的属性放入map field bo
        Map<String,String> requiredAttributeMap = new HashMap<>(); //保存必须传入的属性 map field bo
        for( CustomData customData:customDataList ){
            attributeBOMap.put( customData.getCdField() ,customData.getBo() );
            if( "Y".equals( StrUtil.trim( customData.getIsDataRequired() ) ) ){
                requiredAttributeMap.put( customData.getCdField() ,customData.getBo() );
            }
        }
        //过滤出有效的属性值并且组装数据
        List<CustomDataVal> customDataValList = new ArrayList<>(); //需要维护的数据
        if( customDataValVoList!=null && customDataValVoList.size()>0 ){
            CustomDataVal customDataVal = null;
            for( CustomDataValVo customDataValVo:customDataValVoList ){
                if( attributeBOMap.containsKey( customDataValVo.getAttribute() ) ){ //不存在的属性不保存或更新
                    if( StrUtil.isBlank( customDataValVo.getAttribute() ) || StrUtil.isBlank( customDataValVo.getVals() ) ){
                        //属性为空或者属性值为空不保存
                        continue;
                    }
                    customDataVal = new CustomDataVal();
                    customDataVal.setBo( bo );
                    customDataVal.setCustomDataBo( attributeBOMap.get( customDataValVo.getAttribute() ) );
                    customDataVal.setAttribute( customDataValVo.getAttribute() );
                    customDataVal.setVals( customDataValVo.getVals() );
                    customDataValList.add( customDataVal );
                }
            }
        }

        //保存数据
        if( doMerge ){ //代表属性值合并,没有删除数据操作

            if( customDataValList.size()==0 ){ //无自定义值

                if( requiredAttributeMap.size() >0 ){ //有必填属性值

                    //查询是否已存在必填属性值
                    List<CustomDataVal> existCustomDataValList =getCustomDataValListByBo( bo );
                    if( existCustomDataValList.size() > 0 ){ //可能存在
                        customDataValListIsContainsKeys( requiredAttributeMap, existCustomDataValList );//自定义值list是否包含map中的属性，不包含则报错
                    }else{ //不存在
                        throw new CommonException( "必填属性"+requiredAttributeMap.keySet().stream()
                                .collect( Collectors.joining( "," ) )+"不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
                    }

                }
                //什么都不需要做
                return;
            }else{ //有自定义值

                if( requiredAttributeMap.size() >0 ){ //有必填属性值

                    String[] frontExistKeys = new String[ customDataValList.size() ];
                    for( int i=0; i<customDataValList.size(); i++ ){
                        frontExistKeys[i] = customDataValList.get( i ).getAttribute();
                    }
                    //接受到的参数是否有必填字段，如果没有再查询数据库是否存在已有必填字段值
                    List<String> lackKeyList = new ArrayList<>(); //保存缺少的必填属性
                    for( String key:requiredAttributeMap.keySet() ){
                        if( !ArrayUtil.contains( frontExistKeys,key ) ){
                            lackKeyList.add( key );
                        }
                    }
                    if( lackKeyList.size() > 0 ){ //代表缺少必填属性，需要查询数据库是否已保存
                        List<CustomDataVal> existCustomDataValList =getCustomDataValListByBo( bo );
                        if( existCustomDataValList.size() == 0 ){
                            throw new CommonException( "必填属性"+lackKeyList.stream().collect( Collectors.joining( "," ) )+"不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
                        }
                        customDataValListIsContainsKeys( lackKeyList.stream().collect( Collectors.toMap( Function.identity(),Function.identity(),
                                (key1,key2)->key2 ) ), existCustomDataValList );//自定义值list是否包含map中的属性，不包含则报错

                    }
                }
                customDataValSaveOrUpdate( customDataValList );

            }

        }else{ //不合并，包含删除数据逻辑

            if( customDataValList.size()==0 ){ //代表删除特定对象所有自定义属性值

                if( requiredAttributeMap.size() > 0 ){
                    throw new CommonException( "必填属性"+requiredAttributeMap.keySet().stream()
                            .collect( Collectors.joining( "," ) )+"不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
                }
                List<String> customBoList = new ArrayList<>( attributeBOMap.values() );
                QueryWrapper<CustomDataVal> entityWrapper = new QueryWrapper<>();
                entityWrapper.eq( CustomDataVal.BO, bo ).in( CustomDataVal.CUSTOM_DATA_BO, customBoList );
                super.remove( entityWrapper );

            }else{

                //查询bo保存的自定义数据
                QueryWrapper<CustomDataVal> entityWrapper = new QueryWrapper<>();
                entityWrapper.eq( CustomDataVal.BO, bo ).in( CustomDataVal.CUSTOM_DATA_BO, new ArrayList<>( attributeBOMap.values() ) );
                List<CustomDataVal> existCustomDataValList = super.list( entityWrapper );
                if( requiredAttributeMap.size() > 0 ){
                    customDataValListIsContainsKeys( requiredAttributeMap,customDataValList );//自定义值list是否包含map中的属性，不包含则报错
                }

                if( existCustomDataValList.size() == 0 ){ //代表bo未保存自定义数据，执行保存数据

                    customDataValSaveOrUpdate( customDataValList );

                }else{

                    Map< String,CustomDataVal > exitsCustomDataValMap = new HashMap<>();//已存在的自定义数据值map
                    for( CustomDataVal customDataVal: existCustomDataValList ){
                        exitsCustomDataValMap.put( customDataVal.getAttribute(), customDataVal );
                    }
                    //统计需要新增的数据
                    //统计需要更新的数据
                    //统计需要删除的数据
                    List<CustomDataVal> insertList = new ArrayList<>();
                    List<CustomDataVal> updateList = new ArrayList<>();
                    Map<String,CustomDataVal> updateKeyMap = new HashMap<>();
                    for( CustomDataVal customDataVal:customDataValList ){

                        if( !exitsCustomDataValMap.containsKey( customDataVal.getAttribute() ) ){ //代表新增
                            insertList.add( customDataVal );
                        }else{ //代表更新
                            updateList.add( customDataVal );//更新的对象
                            updateKeyMap.put( customDataVal.getAttribute(), customDataVal );
                        }
                    }

                    //已有数据和需要更新数据的差集就是需要删除的数据
                    List<CustomDataVal> deleteList = new ArrayList<>();
                    for( String attribute: exitsCustomDataValMap.keySet() ){

                        if( !updateKeyMap.containsKey( attribute ) ){
                            deleteList.add( exitsCustomDataValMap.get( attribute ) ); //需要删除的数据
                        }
                    }

                    //新增数据
                    if( insertList.size() > 0 ){
                        customDataValSaveOrUpdate( insertList );
                    }
                    //更新数据
                    if ( updateList.size() > 0 ){
                        customDataValSaveOrUpdate( updateList );
                    }

                    //删除数据
                    if ( deleteList.size() > 0 ){
                        deleteCustomDataVal( deleteList );
                    }

                }

            }
        }

    }

    /**
     * 自定义数据值保存或更新
     *
     * @param customDataValList
     */
    void customDataValSaveOrUpdate( List<CustomDataVal> customDataValList ){
        if( customDataValList==null || customDataValList.size()==0 ){
            return;
        }
        QueryWrapper<CustomDataVal> entityWrapper = null;
        CustomDataVal customDataVal = null;
        for( int i=0; i<customDataValList.size(); i++ ){
            customDataVal = customDataValList.get( i );
            entityWrapper = new QueryWrapper<>();
            entityWrapper.eq( CustomDataVal.BO, customDataVal.getBo() ).eq( CustomDataVal.CUSTOM_DATA_BO, customDataVal.getCustomDataBo() );
            if( !super.update( customDataVal, entityWrapper ) ){ //更新失败则保存
                super.save( customDataVal );
            }
        }
    }

    /**
     * 删除自定义数据值
     *
     * @param customDataValList customDataValList
     */
    void deleteCustomDataVal( List<CustomDataVal> customDataValList ){

        if( customDataValList==null || customDataValList.size()==0 ){
            return;
        }
        List<String> attributeList = new ArrayList<>();
        for ( int i=0; i<customDataValList.size(); i++ ){
            attributeList.add( customDataValList.get( 0 ).getAttribute() );
        }
        QueryWrapper<CustomDataVal> wrapper = new QueryWrapper<>();
        wrapper.eq( CustomDataVal.BO,customDataValList.get( 0 ).getBo() ).in( CustomDataVal.ATTRIBUTE, attributeList );
        super.remove( wrapper );
    }

    /**
     * 删除指定BO自定义数据值
     *
     * @param site 工厂
     * @param bo bo
     * @param customDataTypeEnum 自定义数据类型
     */
    @Override
    public void deleteCustomDataValByBoAndType( String site, String bo, CustomDataTypeEnum customDataTypeEnum ){

        deleteCustomDataValByBoAndType( site, bo,customDataTypeEnum.getDataType() );

    }

    /**
     * 删除指定BO自定义数据值
     *
     * @param site 工厂
     * @param bo bo
     * @param dataType 自定义数据类型
     */
    @Override
    public void deleteCustomDataValByBoAndType( String site, String bo, String dataType ){

        List<CustomData> customDataList = customDataService.getCustomDataListByCustomDataType( site,dataType );
        if( customDataList.size() > 0 ){

            List<String> customDataBoList = new ArrayList<>();
            for( CustomData customData:customDataList ){
                customDataBoList.add( customData.getBo() );
            }
            QueryWrapper<CustomDataVal> wrapper = new QueryWrapper<>();
            wrapper.eq( CustomDataVal.BO,bo ).in( CustomDataVal.CUSTOM_DATA_BO, customDataBoList );
            super.remove( wrapper );

        }

    }

    @Override
    public List<CustomDataAndValVo> selectOnlyCustomData(String site, String bo, String dataType) {
        return customDataValMapper.selectOnlyCustomData(site,bo,dataType);
    }


}