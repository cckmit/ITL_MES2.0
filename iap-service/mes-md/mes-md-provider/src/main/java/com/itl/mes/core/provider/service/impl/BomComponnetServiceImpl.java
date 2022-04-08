package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.BomComponentHandleBO;
import com.itl.mes.core.api.bo.BomHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.entity.BomComponnet;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.service.BomComponnetService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.vo.BomComponnetVo;
import com.itl.mes.core.provider.mapper.BomComponnetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 物料清单组件 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-05
 */
@Service
@Transactional
public class BomComponnetServiceImpl extends ServiceImpl<BomComponnetMapper, BomComponnet> implements BomComponnetService {


    @Autowired
    private BomComponnetMapper bomComponnetMapper;

    @Autowired
    private ItemService itemService;
    @Autowired
    private OperationService operationService;

    @Override
    public List<BomComponnet> selectList() {
        QueryWrapper<BomComponnet> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, bomComponnet);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(String bomBO, BomComponnetVo bomComponnetVo) throws CommonException {
        BomComponnet bomComponnet = new BomComponnet();
        BomComponentHandleBO componnetHandleBO = new BomComponentHandleBO(UserUtils.getSite(),new BomHandleBO(bomBO).getBom(),bomComponnetVo.getComponent(),bomComponnetVo.getItemVersion());
        String bo = componnetHandleBO.getBo();
        ItemHandleBO itemHandleBO = new ItemHandleBO(UserUtils.getSite(),bomComponnetVo.getComponent(),bomComponnetVo.getItemVersion());
        Item exitsItem = itemService.getExitsItemByItemHandleBO(itemHandleBO);
        bomComponnet.setBo(bo);
        bomComponnet.setSite(UserUtils.getSite());
        bomComponnet.setBomBo(bomBO);
        bomComponnet.setSequence(bomComponnetVo.getSequence());
        bomComponnet.setComponentBo(exitsItem.getBo());
        bomComponnet.setItemUnit(bomComponnetVo.getItemUnit());
        if( !StrUtil.isBlank( bomComponnetVo.getOperation() ) && !StrUtil.isBlank( bomComponnetVo.getOperationVersion() )){
            Operation operation = operationService.selectByOperation(bomComponnetVo.getOperation(), bomComponnetVo.getOperationVersion());
            bomComponnet.setOperationBo(operation.getBo());
        }
        bomComponnet.setIsUseAlternateComponent(bomComponnetVo.getIsUseAlternateComponent());
        bomComponnet.setZsType(bomComponnetVo.getZsType());
        bomComponnet.setAssType(bomComponnetVo.getAssType());
        bomComponnet.setComponentPosition(bomComponnetVo.getComponentPosition());
        bomComponnet.setReference(bomComponnetVo.getReference());
        bomComponnet.setQty(bomComponnetVo.getQty());

        bomComponnet.setItemType(bomComponnetVo.getItemType());
        bomComponnet.setItemOrder(bomComponnetVo.getItemOrder());
        bomComponnet.setVirtualItem(bomComponnetVo.getVirtualItem());
        bomComponnet.setItemDesc(bomComponnetVo.getItemDesc());
        bomComponnetMapper.insert(bomComponnet);
    }

    @Override
    public List<BomComponnet> select(String bomBO) {
        QueryWrapper<BomComponnet> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(BomComponnet.SITE, UserUtils.getSite());
        entityWrapper.eq(BomComponnet.BOM_BO,bomBO);
        List<BomComponnet> bomComponnets = bomComponnetMapper.selectList(entityWrapper);
        return bomComponnets;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public int delete(String bomBO) {
        QueryWrapper<BomComponnet> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(BomComponnet.SITE, UserUtils.getSite());
        entityWrapper.eq(BomComponnet.BOM_BO,bomBO);
        Integer deleteInt = bomComponnetMapper.delete(entityWrapper);
        return deleteInt;
    }


}