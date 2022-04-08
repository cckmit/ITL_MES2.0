package com.itl.iap.system.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.api.dto.IapSysRoleTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.entity.IapSysRoleT;
import com.itl.iap.system.api.entity.IapSysUserT;

import java.util.List;

/**
 * 角色Service
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysRoleTService extends IService<IapSysRoleT> {

    /**
     * 分页查询
     *
     * @param iapSysRoleDto 角色实例
     * @return IPage<IapSysRoleTDto>
     */
    IPage<IapSysRoleTDto> pageQuery(IapSysRoleTDto iapSysRoleDto);
    IPage<IapSysRoleTDto> pageQueryByState(IapSysRoleTDto iapSysRoleDto);

    /**
     * lh
     * 2020/6/22 9:39
     * 1 先删除用户和角色的中间表
     * 2 再删除角色和权限的中间表
     * 3 最后删除角色表
     *
     * @param id 要删除的角色id
     * @return boolean
     */
    boolean removeRoleById(String id);

    /**
     * 通过角色id查询用户和所属组织岗位
     *
     * @param iapSysRoleDto 角色实例
     * @return IPage<IapSysUserTDto>
     */
    IPage<IapSysUserTDto> queryUserListByRoleId(IapSysRoleTDto iapSysRoleDto);

    /**
     * 新建角色
     *
     * @param iapSysRoleT 角色实例
     * @return boolean
     * @throws CommonException
     */
    boolean add(IapSysRoleT iapSysRoleT) throws CommonException;

    /**
     * 修改角色
     *
     * @param iapSysRoleT 角色实例
     * @return boolean
     * @throws CommonException
     */
    boolean updateRole(IapSysRoleT iapSysRoleT) throws CommonException;

    /**
     * 通过角色id查询用户和所属组织岗位
     *
     * @param roleId 分页
     * @return List<IapSysUserTDto>
     */
    List<IapSysUserTDto> queryAllUserListByRoleId(String roleId);

    /**
     * 批量删除角色
     *
     * @param sysRoleList 角色列表
     * @return boolean
     */
    boolean deleteBatch(List<IapSysRoleT> sysRoleList);
}