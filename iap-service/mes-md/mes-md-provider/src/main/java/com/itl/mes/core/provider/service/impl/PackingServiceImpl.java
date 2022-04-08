package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.PackingHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.PackLevel;
import com.itl.mes.core.api.entity.Packing;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.PackLevelVo;
import com.itl.mes.core.api.vo.PackingVo;
import com.itl.mes.core.provider.mapper.PackingMapper;
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
 * 包装定义表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-07-12
 */
@Service
@Transactional
public class PackingServiceImpl extends ServiceImpl<PackingMapper, Packing> implements PackingService {


    @Autowired
    private PackingMapper packingMapper;
    @Autowired
    private PackLevelService packLevelService;
    @Autowired
    private CustomDataValService customDataValService;
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private ItemService itemService;
    @Resource
    private UserUtil userUtil;

    @Override
    public List<Packing> selectList() {
        QueryWrapper<Packing> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, packing);
        return super.list(entityWrapper);
    }

    @Override
    public Packing getPackingByPackingHandleBO(PackingHandleBO packingHandleBO) throws CommonException {
        Packing packing = packingMapper.selectById(packingHandleBO.getBo());
        return packing;
    }

    @Override
    public Packing getExitsPackingByPackingHandleBO(PackingHandleBO packingHandleBO) throws CommonException {
        Packing packing = packingMapper.selectById(packingHandleBO.getBo());
        if(packing == null)throw new CommonException("包装:"+packingHandleBO.getPackName()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        return packing;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(PackingVo packingVo) throws CommonException {
        if(packingVo.getPackGrade().equals(""))throw new CommonException("包装等级是必填项", CommonExceptionDefinition.VERIFY_EXCEPTION);
        PackingHandleBO packingHandleBO = new PackingHandleBO(UserUtils.getSite(),packingVo.getPackName());
        Packing packingEntity = getPackingByPackingHandleBO(packingHandleBO);
        Packing packing = new Packing();
        Date newDate = new Date();
        if(packingEntity==null){
            BeanUtils.copyProperties(packingVo,packing);
            packing.setBo(packingHandleBO.getBo());
            packing.setSite(UserUtils.getSite());
            packing.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),newDate);
            packingMapper.insert(packing);
        }else {
            CommonUtil.compareDateSame(packingVo.getModifyDate(),packingEntity.getModifyDate());
            BeanUtils.copyProperties(packingVo,packing);
            packing.setBo(packingHandleBO.getBo());
            packing.setSite(UserUtils.getSite());
            packing.setCreateUser(packingEntity.getCreateUser());
            packing.setCreateDate(packingEntity.getCreateDate());
            packing.setModifyDate(newDate);
            packing.setModifyUser(userUtil.getUser().getUserName());
            packingMapper.updateById(packing);
        }
        //保存自定义数据
        if(packingVo.getCustomDataValVoList() != null&&packingVo.getCustomDataValVoList().size()>0){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(packingHandleBO.getBo());
            customDataValRequest.setSite(UserUtils.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.PACKING.getDataType());
            customDataValRequest.setCustomDataValVoList(packingVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
        if(packingVo.getPackLevelVos()==null)throw new CommonException("包装明细不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        packLevelService.savePackLevels(packingVo.getPackGrade(),packingHandleBO.getBo(),packingVo.getPackLevelVos());

    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public PackingVo getPackingVoByPackName(String packingName) throws CommonException {
        PackingHandleBO packingHandleBO = new PackingHandleBO(UserUtils.getSite(),packingName);
        Packing entityPacking = getExitsPackingByPackingHandleBO(packingHandleBO);
        List<PackLevel> packLevels = packLevelService.getPackLevels(packingHandleBO.getBo());
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), packingHandleBO.getBo(), CustomDataTypeEnum.PACKING.getDataType());
        PackingVo packingVo = new PackingVo();
        BeanUtils.copyProperties(entityPacking,packingVo);
        List<PackLevelVo> packLevelVos = new ArrayList<PackLevelVo>();
        if(packLevels != null) {
            for (PackLevel packLevel:packLevels) {
                PackLevelVo packLevelVo = new PackLevelVo();
                if(packLevel.getPackLevel().equals("P")){
                    packLevelVo = getPackLevelByP(packLevelVo, packLevel);
                }else if(packLevel.getPackLevel().equals("M")){
                    packLevelVo = getPackLevelByM(packLevelVo, packLevel);
                }
                packLevelVos.add(packLevelVo);
            }
        }
        packingVo.setCustomDataAndValVoList(customDataAndValVos);
        packingVo.setPackLevelVos(packLevelVos);
        return packingVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void delete(String packingName,Date modifyDate) throws CommonException {
         PackingHandleBO packingHandleBO = new PackingHandleBO(UserUtils.getSite(),packingName);
        Packing entityPacking = getExitsPackingByPackingHandleBO(packingHandleBO);
        CommonUtil.compareDateSame(modifyDate,entityPacking.getModifyDate());
        packingMapper.deleteById(entityPacking.getBo());
        packLevelService.delete(entityPacking.getBo());

    }

    @Override
    public PackLevelVo getPackLevelByP(PackLevelVo packLevelVo, PackLevel packLevel) {
        BeanUtils.copyProperties(packLevel,packLevelVo);
        Packing packing = packingMapper.selectById(packLevel.getObjectBo());
        packLevelVo.setObject(packing.getPackName());
        if(packLevel.getShopOrderBo()!=null&&!packLevel.getShopOrderBo().equals("")) {
            ShopOrder shopOrder = shopOrderService.getById(packLevel.getShopOrderBo());
            packLevelVo.setShopOrder(shopOrder.getShopOrder());
        }

        return packLevelVo;
    }

    @Override
    public PackLevelVo getPackLevelByM(PackLevelVo packLevelVo, PackLevel packLevel) throws CommonException {
        Item item = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(packLevel.getObjectBo()));
        BeanUtils.copyProperties(packLevel,packLevelVo);
        packLevelVo.setObject(item.getItem());
        packLevelVo.setObjectVersion(item.getVersion());
        if(packLevel.getShopOrderBo()!=null&&!packLevel.getShopOrderBo().equals("")) {
            ShopOrder shopOrder = shopOrderService.getById(packLevel.getShopOrderBo());
            packLevelVo.setShopOrder(shopOrder.getShopOrder());
        }
        return packLevelVo;

    }


}