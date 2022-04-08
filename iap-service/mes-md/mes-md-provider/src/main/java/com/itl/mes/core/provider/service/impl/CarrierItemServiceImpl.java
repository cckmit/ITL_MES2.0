package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.CarrierItemHandleBO;
import com.itl.mes.core.api.bo.ItemGroupHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.entity.CarrierItem;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ItemGroup;
import com.itl.mes.core.api.service.CarrierItemService;
import com.itl.mes.core.api.service.ItemGroupService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.vo.CarrierItemVo;
import com.itl.mes.core.provider.mapper.CarrierItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 载具物料表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-07-22
 */
@Service
@Transactional
public class CarrierItemServiceImpl extends ServiceImpl<CarrierItemMapper, CarrierItem> implements CarrierItemService {


    @Autowired
    private CarrierItemMapper carrierItemMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemGroupService itemGroupService;

    @Override
    public List<CarrierItem> selectList() {
        QueryWrapper<CarrierItem> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, carrierItem);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void save(String carrierTypeBO, List<CarrierItemVo> carrierItemVos) throws CommonException {
        delete(carrierTypeBO);
        if(carrierItemVos != null && carrierItemVos.size()>0){
            int k = CommonUtil.checkForDuplicates(carrierItemVos);
            if(k!=-1)throw new CommonException("装载值:"+carrierItemVos.get(k).getItem()+carrierItemVos.get(k).getItemGroup()+"不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
            int i = 1;
            for(CarrierItemVo carrierItemVo:carrierItemVos){
                CarrierItem carrierItem = new CarrierItem();
                CarrierItemHandleBO carrierItemHandleBO = new CarrierItemHandleBO(carrierTypeBO,String.valueOf(i));
                carrierItem.setBo(carrierItemHandleBO.getBo());
                carrierItem.setCarrierTypeBo(carrierTypeBO);
                if(carrierItemVo.getItem().equals("") && carrierItemVo.getItemVersion().equals("")&&carrierItemVo.getItemGroup().equals(""))throw new CommonException("装载值为必填项", CommonExceptionDefinition.VERIFY_EXCEPTION);
                if(!carrierItemVo.getItem().equals("") && !carrierItemVo.getItemVersion().equals("")){
                    Item exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(UserUtils.getSite(), carrierItemVo.getItem(), carrierItemVo.getItemVersion()));
                    carrierItem.setItemBo(exitsItem.getBo());
                }
                if(!carrierItemVo.getItemGroup().equals("")){
                    ItemGroup itemGroup = itemGroupService.getItemGroupByItemGroupBO(new ItemGroupHandleBO(UserUtils.getSite(), carrierItemVo.getItemGroup()));
                    carrierItem.setItemGroupBo(itemGroup.getBo());
                }
                carrierItem.setSeq(String.valueOf(i));
                carrierItemMapper.insert(carrierItem);
                i++;
            }
        }
    }

    @Override
    public void delete(String carrierTypeBO) throws CommonException {
        QueryWrapper<CarrierItem> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(CarrierItem.CARRIER_TYPE_BO,carrierTypeBO);
        carrierItemMapper.delete(entityWrapper);
    }

    @Override
    public List<CarrierItem> getCarrierItemByCarrierTypeBO(String carrierTypeBO) {
        QueryWrapper<CarrierItem> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(CarrierItem.CARRIER_TYPE_BO,carrierTypeBO);
        List<CarrierItem> carrierItems = carrierItemMapper.selectList(entityWrapper);
        return carrierItems;
    }


}