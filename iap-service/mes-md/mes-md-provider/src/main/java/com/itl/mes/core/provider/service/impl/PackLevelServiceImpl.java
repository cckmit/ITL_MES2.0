package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.PackLevelHandleBO;
import com.itl.mes.core.api.bo.PackingHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.PackLevel;
import com.itl.mes.core.api.entity.Packing;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.PackLevelService;
import com.itl.mes.core.api.service.PackingService;
import com.itl.mes.core.api.service.ShopOrderService;
import com.itl.mes.core.api.vo.PackLevelVo;
import com.itl.mes.core.provider.mapper.PackLevelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 包装级别表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-07-12
 */
@Service
@Transactional
public class PackLevelServiceImpl extends ServiceImpl<PackLevelMapper, PackLevel> implements PackLevelService {


    @Autowired
    private PackLevelMapper packLevelMapper;
    @Autowired
    private PackingService packingService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShopOrderService shopOrderService;

    @Override
    public List<PackLevel> selectList() {
        QueryWrapper<PackLevel> entityWrapper = new QueryWrapper<PackLevel>();
        //getEntityWrapper(entityWrapper, packLevel);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void savePackLevels(String packGrade,String packingBO, List<PackLevelVo> packLevelVos) throws CommonException {
           delete(packingBO);
           if(packLevelVos != null && packLevelVos.size()>0){
               int i = 1;
               for(PackLevelVo packLevelVo:packLevelVos){
                   packLevelVo.setPackingBo(packingBO);
                    if(packLevelVo.getPackLevel().equals("P")){
                        packLevelVo.setSeq(String.valueOf(i));
                        savePackLevelP(packGrade,packLevelVo);
                    }else if(packLevelVo.getPackLevel().equals("M")){
                        packLevelVo.setSeq(String.valueOf(i));
                        savePackLevelM(packLevelVo);
                   }
                   i++;
               }
           }
    }

    @Override
    public void savePackLevelP( String packGrade,PackLevelVo packLevelVo) throws CommonException {
        PackingHandleBO packHandleBO = new PackingHandleBO(packLevelVo.getPackingBo());
        PackLevelHandleBO packLevelHandleBO = new PackLevelHandleBO(UserUtils.getSite(),packHandleBO.getPackName(),packLevelVo.getSeq());
        Packing packing = packingService.getExitsPackingByPackingHandleBO(new PackingHandleBO(UserUtils.getSite(), packLevelVo.getObject()));
        if(!packing.getState().equals("R"))throw new CommonException("子包装:"+packing.getPackName()+"的状态不是可下达", CommonExceptionDefinition.VERIFY_EXCEPTION);
        if(packing ==null)throw new CommonException("包装级别值:"+packLevelVo.getObject()+"不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        if(Integer.parseInt(packing.getPackGrade())<=Integer.parseInt(packGrade))throw new CommonException("不能有包装等级是同级或大于本身的子包装", CommonExceptionDefinition.BASIC_EXCEPTION);
        PackLevel packLevel = new PackLevel();
        BeanUtils.copyProperties(packLevelVo,packLevel);
        packLevel.setBo(packLevelHandleBO.getBo());
        packLevel.setObjectBo(packing.getBo());
        if( packLevelVo.getShopOrder()!=null&&!packLevelVo.getShopOrder().equals("")) {
            ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(UserUtils.getSite(), packLevelVo.getShopOrder()));
            packLevel.setShopOrderBo(existShopOrder.getBo());
        }
        packLevelMapper.insert(packLevel);
    }

    @Override
    public void savePackLevelM( PackLevelVo packLevelVo) throws CommonException {
        PackingHandleBO packHandleBO = new PackingHandleBO(packLevelVo.getPackingBo());
        PackLevelHandleBO packLevelHandleBO = new PackLevelHandleBO(UserUtils.getSite(),packHandleBO.getPackName(),packLevelVo.getSeq());
        Item item = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(UserUtils.getSite(), packLevelVo.getObject(), packLevelVo.getObjectVersion()));

        PackLevel packLevel = new PackLevel();
        BeanUtils.copyProperties(packLevelVo,packLevel);
        packLevel.setBo(packLevelHandleBO.getBo());
        packLevel.setObjectBo(item.getBo());
        if( packLevelVo.getShopOrder()!=null&&!packLevelVo.getShopOrder().equals("")) {
            ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(UserUtils.getSite(), packLevelVo.getShopOrder()));
            packLevel.setShopOrderBo(existShopOrder.getBo());
        }
        packLevelMapper.insert(packLevel);
    }

    @Override
    public void delete(String packingBO) {
        QueryWrapper<PackLevel> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(PackLevel.PACKING_BO,packingBO);
        packLevelMapper.delete(entityWrapper);
    }

    @Override
    public List<PackLevel> getPackLevels(String packingBO) {
        QueryWrapper<PackLevel> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(PackLevel.PACKING_BO,packingBO);
        List<PackLevel> packLevels = packLevelMapper.selectList(entityWrapper);
        return packLevels;
    }

    @Override
    public void IsExits(String packingBO,String objectBO) throws CommonException {
       List<String>  father = null;
       String packingBo = packingBO;
       do{

           for(int i =0;i<father.size();i++){
               packingBo = father.get(i);
               father = packLevelMapper.selectPackingBO(packingBo);
               if(father.equals(objectBO)) throw new CommonException("包装:"+objectBO+"是包装:"+packingBo+"父级包装", CommonExceptionDefinition.BASIC_EXCEPTION);
           }

       } while (father != null);
    }


}