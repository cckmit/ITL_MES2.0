package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.system.api.entity.IapSysUserRoleT;
import com.itl.iap.system.provider.mapper.IapSysUserRoleTMapper;
import com.itl.iap.system.api.service.IapSysUserRoleTService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户角色实现类
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
@Service
public class IapSysUserRoleTServiceImpl extends ServiceImpl<IapSysUserRoleTMapper, IapSysUserRoleT> implements IapSysUserRoleTService {

    @Resource
    private IapSysUserRoleTMapper iapSysUserRoleMapper;

    /**
     * 根据用户ID删除权限
     * @param userId 用户id
     * @return Integer
     */
    @Override
    public Integer deleteRole(String userId){
        return  iapSysUserRoleMapper.deleteByUserId(userId);
    }

}
