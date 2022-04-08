package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.entity.IapSysUserRoleT;

/**
 * 用户角色Service
 *
 * @author 谭强
 * @date 2020-06-24
 * @since jdk1.8
 */
public interface IapSysUserRoleTService extends IService<IapSysUserRoleT>{
    /**
     * 根据用户ID删除权限
     * @param userId 用户id
     * @return Integer
     */
    Integer deleteRole(String userId);
}