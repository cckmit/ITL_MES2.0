package com.itl.mes.core.provider.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.BomHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.BomDto;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.Bom;
import com.itl.mes.core.api.entity.BomComponnet;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.BomComponnetVo;
import com.itl.mes.core.api.vo.BomVo;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.provider.mapper.BomMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 物料清单表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-05
 */
@Service
@Transactional
public class BomServiceImpl extends ServiceImpl<BomMapper, Bom> implements BomService {


    @Autowired
    private BomMapper bomMapper;

    @Autowired
    private BomComponnetService bomComponnetService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OperationService operationService;
    @Autowired
    private CustomDataValService customDataValService;
    @Resource
    private UserUtil userUtil;
    //@Autowired
    //private SfcService sfcService;
    //@Autowired
    //private ShopOrderService shopOrderService;


    @Override
    public List<Bom> selectList() {
        QueryWrapper<Bom> entityWrapper = new QueryWrapper<Bom>();
        //getEntityWrapper(entityWrapper, bom);
        return super.list(entityWrapper);
    }

    @Override
    public IPage<Bom> getBom(BomDto bomDto) {
        if (ObjectUtil.isEmpty(bomDto.getPage())) {
            bomDto.setPage(new Page(0, 10));
        }
        bomDto.setSite(UserUtils.getSite());
        return bomMapper.findBom(bomDto.getPage(), bomDto);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Bom selectByBom(String bom, String version) throws CommonException {
        QueryWrapper<Bom> entityWrapper = new QueryWrapper<Bom>();
        entityWrapper.eq(Bom.SITE, UserUtils.getSite());
        entityWrapper.eq(Bom.BOM, bom);
        entityWrapper.eq(Bom.VERSION, version);
        List<Bom> boms = bomMapper.selectList(entityWrapper);
        if (boms.isEmpty()) {
            throw new CommonException("物料清单" + bom + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            return boms.get(0);
        }

    }

    /**
     * 查询BOM
     *
     * @param bomHandleBO bomHandleBO
     * @return Bom
     */
    @Override
    public Bom getBomByHandleBO(BomHandleBO bomHandleBO) {
        return getById(bomHandleBO.getBo());
    }

    /**
     * 查询存在的BOM，不存在则报错
     *
     * @param bomHandleBO bomHandleBO
     * @return Bom
     * @throws CommonException 异常
     */
    @Override
    public Bom getExitsBomByHandleBO(BomHandleBO bomHandleBO) throws CommonException {
        Bom bom = getBomByHandleBO(bomHandleBO);
        if (bom == null) {
            throw new CommonException("BOM" + bomHandleBO.getBom() + "，版本" + bomHandleBO.getVersion() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return bom;
    }

    /**
     * 通过工厂 物料清单 版本查询物料清单数据
     *
     * @param site
     * @param bom
     * @param version
     * @return
     * @throws CommonException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Bom getRouterByRouterAndVersion(String site, String bom, String version) throws CommonException {

        BomHandleBO bomHandleBO = new BomHandleBO(site, bom, version);
        Bom bomEntity = super.getById(bomHandleBO.getBo());
        if (bomEntity == null) {
            throw new CommonException("物料清单" + bomHandleBO.getBom() + "，版本" + bomHandleBO.getVersion() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return bomEntity;

    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void save(BomVo bomVo) throws CommonException {
        BomHandleBO bomHandleBO = new BomHandleBO(UserUtils.getSite(), bomVo.getBom(), bomVo.getVersion());
        Bom entityBom = bomMapper.selectById(bomHandleBO.getBo());
        if (entityBom == null) {
            Bom bom = new Bom();
            bom.setBo(bomHandleBO.getBo());
            bom.setSite(UserUtils.getSite());
            bom.setBom(bomVo.getBom());
            bom.setBomDesc(bomVo.getBomDesc());
            bom.setState(bomVo.getState());
            bom.setIsCurrentVersion(bomVo.getIsCurrentVersion());
            bom.setVersion(bomVo.getVersion());
            bom.setZsType(bomVo.getZsType());

            bom.setBomStandard(bomVo.getBomStandard());
            bom.setBomType(bomVo.getBomType());
            bom.setErpBom(bomVo.getErpBom());
            if (StringUtils.isNotEmpty(bomVo.getShopOrder())) {
                bom.setShopOrderBo(new ShopOrderHandleBO(UserUtils.getSite(), bomVo.getShopOrder()).getBo());
            } else {
                bom.setShopOrderBo("");
            }

            Date newDate = new Date();
            bom.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(bom);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if (bomVo.getBomComponnetVoList() != null) {
                int i = CommonUtil.checkForDuplicates(bomVo.getBomComponnetVoList());
                if (i != -1) {
                    BomComponnetVo bomComponnetVo = bomVo.getBomComponnetVoList().get(i);
                    throw new CommonException("组件:" + bomComponnetVo.getComponent() + "工序:" + bomComponnetVo.getOperation() + "重复请检查修改后保存", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                for (BomComponnetVo bomComponnetVo : bomVo.getBomComponnetVoList()) {
                    bomComponnetService.save(bomHandleBO.getBo(), bomComponnetVo);
                }
            }
            bomMapper.insert(bom);
            bomVo.setModifyDate(newDate);
        } else {
            Date frontModifyDate = bomVo.getModifyDate();
            Date modifyDate = entityBom.getModifyDate();
            //CommonUtil.compareDateSame(frontModifyDate, modifyDate);
            //todo 注释sfc相关逻辑
            //List<Sfc> sfcs = sfcService.getSfcByBomBO(bomHandleBO.getBo());
            //if(!sfcs.isEmpty())throw new CommonException("物料清单已生产使用不能修改",CommonExceptionDefinition.VERIFY_EXCEPTION);
            Bom bom = new Bom();
            bom.setBo(bomHandleBO.getBo());
            bom.setSite(UserUtils.getSite());
            bom.setBom(bomVo.getBom());
            bom.setBomDesc(bomVo.getBomDesc());
            bom.setState(bomVo.getState());
            bom.setIsCurrentVersion(bomVo.getIsCurrentVersion());
            bom.setVersion(bomVo.getVersion());
            bom.setZsType(bomVo.getZsType());
            Date newDate = new Date();
            bom.setCreateDate(entityBom.getCreateDate());
            bom.setCreateUser(entityBom.getCreateUser());
            bom.setModifyUser(userUtil.getUser().getUserName());

            bom.setBomStandard(bomVo.getBomStandard());
            bom.setBomType(bomVo.getBomType());
            bom.setErpBom(bomVo.getErpBom());
            if (StringUtils.isNotEmpty(bomVo.getShopOrder())) {
                bom.setShopOrderBo(new ShopOrderHandleBO(UserUtils.getSite(), bomVo.getShopOrder()).getBo());
            } else {
                bom.setShopOrderBo("");
            }
            bom.setModifyDate(newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(bom);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            bomComponnetService.delete(bomHandleBO.getBo());
            if (bomVo.getBomComponnetVoList() != null) {
                int i = 1;
                for (BomComponnetVo bomComponnetVo : bomVo.getBomComponnetVoList()) {
                    bomComponnetVo.setSequence(i);
                    bomComponnetService.save(bomHandleBO.getBo(), bomComponnetVo);
                    i++;
                }
            }
            bomMapper.updateById(bom);
            bomVo.setModifyDate(newDate);
        }

        if ("1".equals(bomVo.getIsCurrentVersion())) {
            Bom version2reset = new Bom();
            version2reset.setIsCurrentVersion("0");
            bomMapper.update(version2reset, new QueryWrapper<Bom>().lambda().eq(Bom::getBom, bomVo.getBom()).ne(Bom::getVersion, bomVo.getVersion()));
        }

        if (bomVo.getCustomDataValVoList() != null) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(bomHandleBO.getBo());
            customDataValRequest.setSite(UserUtils.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.BOM.getDataType());
            customDataValRequest.setCustomDataValVoList(bomVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<Bom> select(String bom, String bomDesc, String zsType, String state, String version) {
        QueryWrapper<Bom> entityWrapper = new QueryWrapper<Bom>();
        entityWrapper.eq(Bom.SITE, UserUtils.getSite());
        entityWrapper.like(Bom.BOM, bom);
        entityWrapper.like(Bom.BOM_DESC, bomDesc);
        entityWrapper.like(Bom.ZS_TYPE, zsType);
        entityWrapper.eq(Bom.VERSION, version);
        entityWrapper.like(Bom.STATE, state);
        List<Bom> boms = bomMapper.selectList(entityWrapper);
        return boms;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int delete(String bom, String version, Date modifyDate) throws CommonException {
        BomHandleBO bo = new BomHandleBO(UserUtils.getSite(), bom, version);
        Bom bomEntity = bomMapper.selectById(bo.getBo());
        if (bomEntity == null)
            throw new CommonException("物料清单:" + bom + "版本:" + version + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        CommonUtil.compareDateSame(modifyDate, bomEntity.getModifyDate());
        //todo 注释sfc和工单逻辑
        //List<Sfc> sfcs = sfcService.getSfcByBomBO(bomEntity.getBo());
        //if(!sfcs.isEmpty())throw new CommonException("物料清单已生产使用不能删除",CommonExceptionDefinition.BASIC_EXCEPTION);
        List<Item> items = itemService.getItemByBomBO(bomEntity.getBo());
        if (!items.isEmpty()) throw new CommonException("物料清单已使用不能删除", CommonExceptionDefinition.BASIC_EXCEPTION);
        //List<ShopOrder> shopOrders = shopOrderService.getShopOrderByBomBO(bomEntity.getBo());
        //if(!shopOrders.isEmpty())throw new CommonException("物料清单已使用不能删除",CommonExceptionDefinition.BASIC_EXCEPTION);
        bomComponnetService.delete(bomEntity.getBo());
        Integer deleteInt = bomMapper.deleteById(bomEntity.getBo());
        return deleteInt;
    }

    @Override
    public BomVo getBomVoByBomAndVersion(String bom, String version) throws CommonException {
        Bom bomEntity = selectByBom(bom, version);
        BomVo bomVo = new BomVo();
        //拿到物料组件信息
        List<BomComponnet> bomComponnets = bomComponnetService.select(bomEntity.getBo());
        List<BomComponnetVo> bomComponnetVos = new ArrayList<BomComponnetVo>();
        if (!bomComponnets.isEmpty()) {
            for (BomComponnet bomComponnet : bomComponnets) {
                BomComponnetVo bomComponnetVo = new BomComponnetVo();
                //获取item信息
                Item item = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(bomComponnet.getComponentBo()));
                if (bomComponnet.getOperationBo() != null) {
                    Operation operation = operationService.getById(bomComponnet.getOperationBo());
                    operation = Optional.ofNullable(operation).orElse(new Operation());
                    bomComponnetVo.setOperation(operation.getOperation());
                    bomComponnetVo.setOperationVersion(operation.getVersion());
                }
                BeanUtils.copyProperties(bomComponnet, bomComponnetVo);
                bomComponnetVo.setBom(bomEntity.getBom());
                bomComponnetVo.setComponent(item.getItem());
                bomComponnetVo.setItemVersion(item.getVersion());
                bomComponnetVo.setItemDesc(item.getItemDesc());
                bomComponnetVos.add(bomComponnetVo);
            }
        }
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), bomEntity.getBo(), CustomDataTypeEnum.BOM.getDataType());
        BeanUtils.copyProperties(bomEntity, bomVo);
        bomVo.setBomComponnetVoList(bomComponnetVos);
        bomVo.setCustomDataAndValVoList(customDataAndValVos);
        if (StringUtils.isNotEmpty(bomEntity.getShopOrderBo())) {
            bomVo.setShopOrder(new ShopOrderHandleBO(bomEntity.getShopOrderBo()).getShopOrder());
        }
        return bomVo;
    }

    @Override
    public BomVo getBomVoByBo(String bo) throws CommonException {
        String site = new BomHandleBO(bo).getSite();
        Bom bomEntity = bomMapper.selectById(bo);
        BomVo bomVo = new BomVo();
        LambdaQueryWrapper<BomComponnet> query = new QueryWrapper<BomComponnet>().lambda()
                .eq(BomComponnet::getSite, site)
                .eq(BomComponnet::getBomBo, bo);
        List<BomComponnet> bomComponnets = bomComponnetService.list(query);
        List<BomComponnetVo> bomComponnetVos = new ArrayList<BomComponnetVo>();
        if (!bomComponnets.isEmpty()) {
            for (BomComponnet bomComponnet : bomComponnets) {
                BomComponnetVo bomComponnetVo = new BomComponnetVo();
                Item item = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(bomComponnet.getComponentBo()));
                if (bomComponnet.getOperationBo() != null) {
                    Operation operation = operationService.getById(bomComponnet.getOperationBo());
                    bomComponnetVo.setOperation(Optional.ofNullable(operation).map(o -> o.getOperation()).orElse(null));
                    bomComponnetVo.setOperationVersion(Optional.ofNullable(operation).map(o -> o.getVersion()).orElse(null));
                }
                BeanUtils.copyProperties(bomComponnet, bomComponnetVo);
                bomComponnetVo.setBom(bomEntity.getBom());
                bomComponnetVo.setComponent(item.getItem());
                bomComponnetVo.setItemVersion(item.getVersion());
                bomComponnetVo.setItemDesc(item.getItemDesc());
                bomComponnetVos.add(bomComponnetVo);
            }
        }
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(site, bomEntity.getBo(), CustomDataTypeEnum.BOM.getDataType());
        BeanUtils.copyProperties(bomEntity, bomVo);
        bomVo.setBomComponnetVoList(bomComponnetVos);
        bomVo.setCustomDataAndValVoList(customDataAndValVos);
        if (StringUtils.isNotEmpty(bomEntity.getShopOrderBo())) {
            bomVo.setShopOrder(new ShopOrderHandleBO(bomEntity.getShopOrderBo()).getShopOrder());
        }
        return bomVo;
    }
}
