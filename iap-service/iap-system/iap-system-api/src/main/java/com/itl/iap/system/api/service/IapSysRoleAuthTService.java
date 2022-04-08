package com.itl.iap.system.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapSysRoleAuthTDto;
import com.itl.iap.system.api.entity.IapSysRoleAuthT;

import java.util.List;

/**
 * 角色权限中间表Service
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysRoleAuthTService extends IService<IapSysRoleAuthT> {

    /**
     * 分页查询
     *
     * @param iapSysRoleAuthDto 角色权限实例
     * @return IPage<IapSysRoleAuthTDto>
     */
    IPage<IapSysRoleAuthTDto> pageQuery(IapSysRoleAuthTDto iapSysRoleAuthDto);

    /**
     * 分配权限，1 先批删 2 再批增
     *
     * @param iapSysRoleAuthT 角色权限实例
     * @return boolean
     */
    boolean batchRemoveAndAdd(List<IapSysRoleAuthT> iapSysRoleAuthT);
}