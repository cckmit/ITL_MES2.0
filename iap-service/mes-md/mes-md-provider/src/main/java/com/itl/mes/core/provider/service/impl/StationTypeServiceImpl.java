package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.StationTypeHandleBO;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.StationType;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.service.StationService;
import com.itl.mes.core.api.service.StationTypeItemService;
import com.itl.mes.core.api.service.StationTypeService;
import com.itl.mes.core.api.vo.StationTypeItemVo;
import com.itl.mes.core.api.vo.StationTypeVo;
import com.itl.mes.core.provider.mapper.StationTypeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工位类型表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@Service
@Transactional
public class StationTypeServiceImpl extends ServiceImpl<StationTypeMapper, StationType> implements StationTypeService {


    @Autowired
    private StationTypeMapper stationTypeMapper;
    @Autowired
    private StationTypeItemService stationTypeItemService;
    @Autowired
    private StationService stationService;
    @Autowired
    private OperationService operationService;
    @Resource
    private UserUtil userUtil;

    @Override
    public List<StationType> selectList() {
        QueryWrapper<StationType> entityWrapper = new QueryWrapper<StationType>();
        //getEntityWrapper(entityWrapper, stationType);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(StationTypeVo stationTypeVo) throws CommonException {
          StationTypeHandleBO stationTypeBO = new StationTypeHandleBO(UserUtils.getSite(),stationTypeVo.getStationType());
          String bo = stationTypeBO.getBo();
        StationType entityTationType = stationTypeMapper.selectById(bo);
        if(entityTationType==null){
            StationType stationType = new StationType();
            stationType.setBo(bo);
            stationType.setSite(UserUtils.getSite());
            stationType.setStationType(stationTypeVo.getStationType());
            stationType.setStationTypeName(stationTypeVo.getStationTypeName());
            stationType.setStationTypeDesc(stationTypeVo.getStationTypeDesc());
            Date newDate = new Date();
            stationType.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(stationType);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            stationTypeMapper.insert(stationType);
            stationTypeVo.setModifyDate(newDate);
        }else{
            Date frontModifyDate = stationTypeVo.getModifyDate();
            Date modifyDate = entityTationType.getModifyDate();
            CommonUtil.compareDateSame(frontModifyDate,modifyDate);
            StationType stationType = new StationType();
            stationType.setBo(bo);
            stationType.setSite(UserUtils.getSite());
            stationType.setStationType(stationTypeVo.getStationType());
            stationType.setStationTypeName(stationTypeVo.getStationTypeName());
            stationType.setStationTypeDesc(stationTypeVo.getStationTypeDesc());
            stationType.setCreateUser(entityTationType.getCreateUser());
            stationType.setCreateDate(entityTationType.getCreateDate());
            stationType.setModifyUser(userUtil.getUser().getUserName());
            Date newDate = new Date();
            stationType.setModifyDate(newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(stationType);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            List<Operation> operations = operationService.getOperationByStationTypeBO(bo);
            if(!operations.isEmpty())throw new CommonException("工序类型:"+stationTypeBO.getStationType()+"已使用，不能修改", CommonExceptionDefinition.BASIC_EXCEPTION);
            stationTypeMapper.updateById(stationType);

        }

        stationTypeItemService.save(stationTypeBO.getBo(),stationTypeVo.getAssignedStationTypeItemVos());

    }

    @Override
    public StationType selectByStationType(String stationType) throws CommonException {
        QueryWrapper<StationType> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(StationType.SITE,UserUtils.getSite());
        entityWrapper.eq(StationType.STATION_TYPE,stationType);
        List<StationType> stationTypes = stationTypeMapper.selectList(entityWrapper);
        if(stationTypes.isEmpty()){
            throw new CommonException("工序类型:"+stationType+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return stationTypes.get(0);
        }
    }

    @Override
    public StationTypeVo selectStationTypeVo(String stationType) throws CommonException {
        StationType stationTypeEntity = selectByStationType(stationType);
        List<StationTypeItemVo> availableStationTypeItemVos = stationTypeMapper.getAvailableStationTypeItemVos(UserUtils.getSite(), null, stationTypeEntity.getBo());
        List<StationTypeItemVo> assignedStationTypeItemVos = stationTypeMapper.getAssignedStationTypeItemVos(UserUtils.getSite(), stationTypeEntity.getBo());
        StationTypeVo stationTypeVo = new StationTypeVo();
        BeanUtils.copyProperties(stationTypeEntity,stationTypeVo);
        stationTypeVo.setAvailableStationTypeItemVos(availableStationTypeItemVos);
        stationTypeVo.setAssignedStationTypeItemVos(assignedStationTypeItemVos);
        return stationTypeVo;
    }


    @Override
    public List<StationType> selectStationType(String stationType, String stationTypeName, String stationTypeDesc) {
        QueryWrapper<StationType> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(StationType.SITE,UserUtils.getSite());
        entityWrapper.like(StationType.STATION_TYPE,stationType);
        entityWrapper.like(StationType.STATION_TYPE_NAME,stationTypeName);
        entityWrapper.like(StationType.STATION_TYPE_DESC,stationTypeDesc);
        List<StationType> stationTypes = stationTypeMapper.selectList(entityWrapper);
        return stationTypes;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public int delete(String stationType ,Date modifyDate ) throws CommonException {
        StationType stationTypeEntity = selectByStationType(stationType);
        CommonUtil.compareDateSame(modifyDate,stationTypeEntity.getModifyDate());
        List<Operation> operations = operationService.getOperationByStationTypeBO(stationTypeEntity.getBo());
        if(!operations.isEmpty())throw new CommonException("工序类型:"+stationType+"已使用，不能修改", CommonExceptionDefinition.BASIC_EXCEPTION);
        stationTypeItemService.delete(stationTypeEntity.getBo());
        Integer delete = stationTypeMapper.deleteById(stationTypeEntity.getBo());
        return delete;
    }

    @Override
    public List<StationTypeItemVo> getStationTypeItemVo(String station)  {
        List<StationTypeItemVo> stationTypeItemVos = stationTypeMapper.getAvailableStationTypeItemVos(UserUtils.getSite(), station, null);
        return stationTypeItemVos;
    }


}