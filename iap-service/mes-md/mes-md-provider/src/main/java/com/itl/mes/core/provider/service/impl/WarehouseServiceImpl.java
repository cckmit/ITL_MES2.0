package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.WarehouseHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.Warehouse;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.WarehouseService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.WarehouseVo;
import com.itl.mes.core.provider.mapper.WarehouseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 线边仓信息表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-18
 */
@Service
@Transactional
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {


    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private CustomDataValService customDataValService;
    @Resource
    private UserUtil userUtil;

    @Override
    public List<Warehouse> selectList() {
        QueryWrapper<Warehouse> entityWrapper = new QueryWrapper<Warehouse>();
        //getEntityWrapper(entityWrapper, warehouse);
        return super.list(entityWrapper);
    }

    @Override
    public Warehouse selectWarehouse(String warehouse) throws CommonException {
        QueryWrapper<Warehouse> entityWrapper = new QueryWrapper<Warehouse>();
        entityWrapper.eq(Warehouse.SITE, UserUtils.getSite());
        entityWrapper.eq(Warehouse.WAREHOUSE,warehouse);
        List<Warehouse> warehouses = warehouseMapper.selectList(entityWrapper);
        if(warehouses.isEmpty()){
            throw new CommonException("线边仓:"+warehouse+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return  warehouses.get(0);
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(WarehouseVo warehouseVo) throws CommonException {
        WarehouseHandleBO handleBO = new WarehouseHandleBO(UserUtils.getSite(),warehouseVo.getWarehouse());
        Warehouse ware = warehouseMapper.selectById(handleBO.getBo());
        Warehouse warehouse = new Warehouse();
        if(ware == null){
             BeanUtils.copyProperties(warehouseVo,warehouse);
             warehouse.setBo(handleBO.getBo());
             warehouse.setSite(UserUtils.getSite());
             Date newDate = new Date();
             warehouse.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),newDate);
             ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(warehouse);
             if(validResult.isHasErrors()){
                 throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
             }
             warehouseMapper.insert(warehouse);
        }else {
            CommonUtil.compareDateSame(warehouseVo.getModifyDate(),ware.getModifyDate());
            BeanUtils.copyProperties(warehouseVo,warehouse);
            warehouse.setBo(ware.getBo());
            warehouse.setSite(UserUtils.getSite());
            warehouse.setCreateUser(ware.getCreateUser());
            warehouse.setCreateDate(ware.getCreateDate());
            Date newDate = new Date();
            warehouse.setModifyDate(newDate);
            warehouse.setModifyUser(userUtil.getUser().getUserName());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(warehouse);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            warehouseMapper.updateById(warehouse);
        }

        if(warehouseVo.getCustomDataValVoList() != null) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setSite(UserUtils.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.WAREHOUSE.getDataType());
            customDataValRequest.setBo(handleBO.getBo());
            customDataValRequest.setCustomDataValVoList(warehouseVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void delete(String warehouse, Date modifyDate) throws CommonException {
        Warehouse warehouseEntity = selectWarehouse(warehouse);
        CommonUtil.compareDateSame(modifyDate,warehouseEntity.getModifyDate());
        warehouseMapper.deleteById(warehouseEntity.getBo());
    }

    @Override
    public WarehouseVo getWarehouseVo(String warehouse) throws CommonException {
        Warehouse warehouseEntity = selectWarehouse(warehouse);
        WarehouseVo warehouseVo = new WarehouseVo();
        BeanUtils.copyProperties(warehouseEntity,warehouseVo);
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), warehouseEntity.getBo(), CustomDataTypeEnum.WAREHOUSE.getDataType());
        warehouseVo.setCustomDataAndValVoList(customDataAndValVos);
        return warehouseVo;
    }



}