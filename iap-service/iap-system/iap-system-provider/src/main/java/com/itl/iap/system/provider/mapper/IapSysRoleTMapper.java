package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.dto.IapSysRoleTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.entity.IapSysRoleT;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色mapper
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysRoleTMapper extends BaseMapper<IapSysRoleT> {

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param iapSysRoleTDto 角色实例
     * @return IPage<IapSysRoleTDto>
     */
    IPage<IapSysRoleTDto> pageQuery(Page page, @Param("iapSysRoleTDto") IapSysRoleTDto iapSysRoleTDto);
    IPage<IapSysRoleTDto> pageQueryByState(Page page, @Param("iapSysRoleTDto") IapSysRoleTDto iapSysRoleTDto);

    /**
     * 通过角色id查询用户和所属组织岗位
     *
     * @param page           分页
     * @param iapSysRoleTDto 角色实例
     * @return IPage<IapSysUserTDto>
     */
    IPage<IapSysUserTDto> selectUserListByRoleId(Page page, @Param("iapSysRoleTDto") IapSysRoleTDto iapSysRoleTDto);

    /**
     * 通过角色id查询用户和所属组织岗位
     *
     * @param roleId 分页
     * @return List<IapSysUserTDto>
     */
    List<IapSysUserTDto> queryAllUserListByRoleId(@Param("roleId") String roleId);

    /**
     * 批量删除角色列表
     *
     * @param sysRoleList 角色列表
     * @return boolean
     */
    boolean deleteBatchByRoleIdList(@Param("sysRoleList") List<IapSysRoleT> sysRoleList);
}