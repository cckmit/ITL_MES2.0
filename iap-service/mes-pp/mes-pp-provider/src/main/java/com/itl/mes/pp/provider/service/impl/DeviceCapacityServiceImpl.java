package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.pp.api.dto.ProductLineDto;
import com.itl.mes.pp.api.entity.DeviceCapacityEntity;
import com.itl.mes.pp.api.service.DeviceCapacityService;
import com.itl.mes.pp.provider.mapper.DeviceCapacityMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 崔翀赫
 * @date 2020/12/18 14:44
 * @since JDK1.8
 */
@Service("ProductLineService")
public class DeviceCapacityServiceImpl extends ServiceImpl<DeviceCapacityMapper, DeviceCapacityEntity> implements DeviceCapacityService {

    @Autowired
    private DeviceCapacityMapper deviceCapacityMapper;
    @Autowired
    private UserUtil userUtil;

    @Override
    public IPage<DeviceCapacityEntity> getAll(ProductLineDto productLineDto) {
        List<DeviceCapacityEntity> p = deviceCapacityMapper.getAll(UserUtils.getSite());
        p.forEach(x -> {
            if (StringUtils.isNotEmpty(x.getProductLineBo())) {
                x.setProductLineBo(new ProductLineHandleBO(x.getProductLineBo()).getProductLine());
            }
            if (StringUtils.isNotEmpty(x.getDeviceBo())) {
                x.setDeviceBo(new DeviceHandleBO(x.getDeviceBo()).getDevice());
            }
            if (StringUtils.isNotEmpty(x.getWorkShopBo())) {
                x.setWorkShopBo(new WorkShopHandleBO(x.getWorkShopBo()).getWorkShop());
            }
        });


        if (StringUtils.isNotEmpty(productLineDto.getWorkShop())) {
            p = p.stream().filter(x -> x.getWorkShopBo().contains(productLineDto.getWorkShop())).collect(Collectors.toList());
        }
        if (StringUtils.isNoneBlank(productLineDto.getProductLineBo())) {
            p = p.stream().filter(x -> x.getProductLineBo().contains(productLineDto.getProductLineBo())).collect(Collectors.toList());
        }
        List<DeviceCapacityEntity> collect1 = null;
        if (productLineDto.getPage() != 0 && productLineDto.getLimit() != 0) {
            collect1 = p.stream().skip(productLineDto.getLimit() * (productLineDto.getPage() - 1)).limit(productLineDto.getLimit()).collect(Collectors.toList());
        } else if (productLineDto.getPage() == 0 && productLineDto.getLimit() == 0) {
            collect1 = p.stream().limit(10).collect(Collectors.toList());
        }
        Page page = new Page();
        page.setTotal(CollectionUtil.isEmpty(p) ? 0 : p.size());
        page.setRecords(collect1);
        page.setSize(productLineDto.getLimit() == 0 ? 10 : productLineDto.getLimit());
        page.setPages(page.getTotal() % page.getSize() == 0 ? page.getTotal() / page.getSize() : page.getTotal() / page.getSize() + 1);
        page.setCurrent(productLineDto.getPage() == 0 ? 1 : productLineDto.getPage());

        return page;
    }

    @Override
    public void saveProductLine(DeviceCapacityEntity productLineEntities) throws CommonException {

        if (StringUtils.isNotEmpty(productLineEntities.getWorkShopBo())) {
            productLineEntities.setWorkShopBo(new WorkShopHandleBO(UserUtils.getSite(), productLineEntities.getWorkShopBo()).getBo());
        }
        if (StringUtils.isNotEmpty(productLineEntities.getProductLineBo())) {
            productLineEntities.setProductLineBo(new ProductLineHandleBO(UserUtils.getSite(), productLineEntities.getProductLineBo()).getBo());
        }
        if (StringUtils.isNotEmpty(productLineEntities.getDeviceBo())) {
            productLineEntities.setDeviceBo(new DeviceHandleBO(UserUtils.getSite(), productLineEntities.getDeviceBo()).getBo());
        }
        if (StringUtils.isEmpty(productLineEntities.getBo())) {
            productLineEntities.setBo(UUID.uuid32());
            productLineEntities.setSite(UserUtils.getSite());
            productLineEntities.setCreateDate(new Date());
            productLineEntities.setCreateUser(userUtil.getUser().getUserName());
            List<DeviceCapacityEntity> deviceCapacityEntities = deviceCapacityMapper.selectList(new QueryWrapper<DeviceCapacityEntity>().lambda().eq(DeviceCapacityEntity::getDeviceBo, productLineEntities.getDeviceBo()));

            if(CollectionUtil.isNotEmpty(deviceCapacityEntities)){
                throw new CommonException( "机台重复,无法保存", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            deviceCapacityMapper.insert(productLineEntities);
        } else {
            productLineEntities.setModifyDate(new Date());
            productLineEntities.setModifyUser(userUtil.getUser().getUserName());
            deviceCapacityMapper.updateById(productLineEntities);
        }

    }
}