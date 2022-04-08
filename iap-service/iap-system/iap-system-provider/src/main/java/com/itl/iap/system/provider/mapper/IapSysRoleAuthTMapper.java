package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.dto.IapSysRoleAuthTDto;
import com.itl.iap.system.api.entity.IapSysAuthT;
import com.itl.iap.system.api.entity.IapSysRoleAuthT;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.entity.IapSysRoleT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限中间表mapper
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysRoleAuthTMapper extends BaseMapper<IapSysRoleAuthT> {

    /**
     * 分页查询
     *
     * @param page               分页对象
     * @param iapSysRoleAuthTDto 角色权限实例
     * @return IPage<IapSysRoleAuthTDto>
     */
    IPage<IapSysRoleAuthTDto> pageQuery(Page page, @Param("iapSysRoleAuthTDto") IapSysRoleAuthTDto iapSysRoleAuthTDto);

    /**
     * 批量插入
     *
     * @param list 角色权限实例列表
     * @return boolean
     */
    boolean insertBatch(List<IapSysRoleAuthT> list);

    /**
     * 通过角色列表批量删除角色-权限中间表
     *
     * @param sysRoleList 角色列表
     * @return boolean
     */
    boolean deleteBatchByRoleIdList(@Param("sysRoleList") List<IapSysRoleT> sysRoleList);

    /**
     * 通过权限列表批量删除角色-权限中间表
     *
     * @param sysAuthList 权限列表
     * @return boolean
     */
    boolean deleteBatchByAuthIdList(@Param("sysAuthList") List<IapSysAuthT> sysAuthList);
}