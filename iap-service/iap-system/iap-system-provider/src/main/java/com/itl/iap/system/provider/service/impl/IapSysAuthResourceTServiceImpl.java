package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.entity.IapSysAuthResourceT;
import com.itl.iap.system.provider.mapper.IapSysAuthResourceTMapper;
import com.itl.iap.system.api.service.IapSysAuthResourceTService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户菜单实现类
 *
 * @author 谭强
 * @date 2020-06-22
 * @since jdk1.8
 */
@Service
public class IapSysAuthResourceTServiceImpl extends ServiceImpl<IapSysAuthResourceTMapper, IapSysAuthResourceT> implements IapSysAuthResourceTService {

    @Resource
    private IapSysAuthResourceTMapper iapSysAuthResourceMapper;

    /**
     * 权限菜单添加
     *
     * @param authResourceTs 权限角色实例
     * @param authId         权限id
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAuthResource(List<IapSysAuthResourceT> authResourceTs, String authId) {
        List<IapSysAuthResourceT> having = iapSysAuthResourceMapper.selectList(new QueryWrapper<IapSysAuthResourceT>().lambda().eq(IapSysAuthResourceT::getAuthId, authId))
                .stream()
                .collect(Collectors.toList());

        List<String> haveRes = having.stream().map(IapSysAuthResourceT::getResourceId).collect(Collectors.toList());
        List<String> toSave = authResourceTs.stream().map(IapSysAuthResourceT::getResourceId).collect(Collectors.toList());


        List<IapSysAuthResourceT> toAdd = authResourceTs.stream()
                .filter(x -> !haveRes.contains(x.getResourceId()))
                .map(x -> x.setId(UUID.uuid32()).setDelFlag(Short.valueOf("0"))).collect(Collectors.toList());

        List<String> toDelete = having.stream().filter(x -> !toSave.contains(x.getResourceId()))
                .map(IapSysAuthResourceT::getId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(toDelete)) {
            iapSysAuthResourceMapper.deleteBatchIds(toDelete);
        }
        if (CollectionUtil.isNotEmpty(toAdd)) {
            this.saveBatch(toAdd);
        }
        return true;


//        boolean bork = false;
//        authResourceTs.forEach(iapSysAuthResourceT -> {
//            iapSysAuthResourceT.setDelFlag((short)0);
//        });
//        bork = iapSysAuthResourceMapper.deleteByAuthId(authId) != 0 ? true : false;
//        if (!authResourceTs.isEmpty()) {
//            authResourceTs.forEach(x -> {
//                if (x.getId() == null) {
//                    x.setId(UUID.uuid32());
//                }
//            });
//            bork = this.saveBatch(authResourceTs);
//        }
//        return bork;
    }
}
