package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.CarrierTypeHandleBO;
import com.itl.mes.core.api.bo.ItemGroupHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.entity.CarrierItem;
import com.itl.mes.core.api.entity.CarrierType;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ItemGroup;
import com.itl.mes.core.api.service.CarrierItemService;
import com.itl.mes.core.api.service.CarrierTypeService;
import com.itl.mes.core.api.service.ItemGroupService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.vo.CarrierItemVo;
import com.itl.mes.core.api.vo.CarrierTypeVo;
import com.itl.mes.core.provider.mapper.CarrierTypeMapper;
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
 * 载具类型表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-07-22
 */
@Service
@Transactional
public class CarrierTypeServiceImpl extends ServiceImpl<CarrierTypeMapper, CarrierType> implements CarrierTypeService {


    @Autowired
    private CarrierTypeMapper carrierTypeMapper;
    @Autowired
    private CarrierItemService carrierItemService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemGroupService itemGroupService;
    @Resource
    private UserUtil userUtil;

    @Override
    public List<CarrierType> selectList() {
        QueryWrapper<CarrierType> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, carrierType);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void save(CarrierTypeVo carrierTypeVo) throws CommonException {
        CarrierTypeHandleBO carrierTypeHandleBO = new CarrierTypeHandleBO(UserUtils.getSite(),carrierTypeVo.getCarrierType());
        CarrierType carrierTypeEntity = getCarrierType(carrierTypeHandleBO);
        CarrierType carrierType = new CarrierType();
        BeanUtils.copyProperties(carrierTypeVo,carrierType);
        carrierType.setBo(carrierTypeHandleBO.getBo());
        carrierType.setSite(UserUtils.getSite());
        carrierType.setCapacity(Integer.valueOf(carrierType.getCapacity()));
        carrierType.setRowSize(Integer.valueOf(carrierType.getRowSize()));
        carrierType.setColumnSize(Integer.valueOf(carrierType.getColumnSize()));
        carrierType.setIsBlend(carrierTypeVo.getIsBlend());
        if(carrierTypeEntity==null){
            carrierType.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),new Date());
            carrierTypeMapper.insert(carrierType);
        }else{
            CommonUtil.compareDateSame(carrierTypeVo.getModifyDate(),carrierTypeEntity.getModifyDate());
             carrierType.setCreateUser(carrierTypeEntity.getCreateUser());
             carrierType.setCreateDate(carrierTypeEntity.getCreateDate());
             carrierType.setModifyUser(userUtil.getUser().getUserName());
             carrierType.setModifyDate(new Date());
             carrierTypeMapper.updateById(carrierType);
        }
        carrierItemService.save(carrierTypeHandleBO.getBo(),carrierTypeVo.getCarrierItemVos());

    }

    @Override
    public CarrierTypeVo getCarrierTypeVoByCarrierType(String carrierType) throws CommonException {
        CarrierType exitsCarrierType = getExitsCarrierType(new CarrierTypeHandleBO(UserUtils.getSite(), carrierType));
        List<CarrierItem> carrierItems = carrierItemService.getCarrierItemByCarrierTypeBO(exitsCarrierType.getBo());
        List<CarrierItemVo> carrierItemVos = new ArrayList<CarrierItemVo>();
        if(carrierItems != null){
           for(CarrierItem carrierItem :carrierItems){
               CarrierItemVo carrierItemVo = new CarrierItemVo();
               if(carrierItem.getItemBo() != null){
                   if (StrUtil.isNotBlank(carrierItem.getItemBo())){
                       Item exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(carrierItem.getItemBo()));
                       carrierItemVo.setItem(exitsItem.getItem());
                       carrierItemVo.setItemVersion(exitsItem.getVersion());
                       carrierItemVo.setCarrierItemType("I");
                       ValidationUtil.nullToEmpty(carrierItemVo);
                       carrierItemVos.add(carrierItemVo);
                   }
               }
               if(carrierItem.getItemGroupBo() !=null){
                   if (StrUtil.isNotBlank(carrierItem.getItemGroupBo())){
                       ItemGroup itemGroup = itemGroupService.getItemGroupByItemGroupBO(new ItemGroupHandleBO(carrierItem.getItemGroupBo()));
                       carrierItemVo.setItemGroup(itemGroup.getItemGroup());
                       carrierItemVo.setCarrierItemType("G");
                       ValidationUtil.nullToEmpty(carrierItemVo);
                       carrierItemVos.add(carrierItemVo);
                   }
               }
           }
        }
        CarrierTypeVo carrierTypeVo = new CarrierTypeVo();
        BeanUtils.copyProperties(exitsCarrierType,carrierTypeVo);
        carrierTypeVo.setCarrierItemVos(carrierItemVos);
        return carrierTypeVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void delete(String carrierType,Date modifyDate) throws CommonException {
        CarrierType exitsCarrierType = getExitsCarrierType(new CarrierTypeHandleBO(UserUtils.getSite(), carrierType));
        CommonUtil.compareDateSame(modifyDate,exitsCarrierType.getModifyDate());
        carrierTypeMapper.deleteById(exitsCarrierType.getBo());
        carrierItemService.delete(exitsCarrierType.getBo());
    }

    @Override
    public CarrierType getCarrierType(CarrierTypeHandleBO carrierTypeHandleBO) throws CommonException {
        CarrierType carrierTypeEntity = carrierTypeMapper.selectById(carrierTypeHandleBO.getBo());
        return carrierTypeEntity;
    }

    @Override
    public CarrierType getExitsCarrierType(CarrierTypeHandleBO carrierTypeHandleBO) throws CommonException {
        CarrierType carrierTypeEntity = carrierTypeMapper.selectById(carrierTypeHandleBO.getBo());
        if(carrierTypeEntity == null){
            throw new CommonException("载具类型:"+carrierTypeHandleBO.getCarrierType()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return carrierTypeEntity;
        }

    }


}