package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.entity.IapSysRoleT;
import com.itl.iap.system.api.entity.IapSysUserRoleT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色mapper
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
public interface IapSysUserRoleTMapper extends BaseMapper<IapSysUserRoleT> {

    /**
     * 根据用户ID删除权限
     * @param userId 用户id
     * @return Integer
     */
     Integer deleteByUserId(@Param("userId") String userId);

    /**
     * 通过传入的角色列表批量删除用户-角色中间表
     * @param sysRoleList 角色列表
     */
    void deleteBatchByRoleIdList(@Param("sysRoleList") List<IapSysRoleT> sysRoleList);
}