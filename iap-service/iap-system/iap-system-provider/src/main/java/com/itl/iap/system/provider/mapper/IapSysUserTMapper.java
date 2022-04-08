package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapSysRoleTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.dto.req.IapRoleUserQueryDTO;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import com.itl.iap.system.api.entity.IapSysUserT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper
 *
 * @author 谭强
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysUserTMapper extends BaseMapper<IapSysUserT> {

    /**
     * 分页查询
     *
     * @return IPage<IapSysUserTDto>
     */
    IPage<IapSysUserTDto> pageQuery(Page page,@Param("iapSysUserTDto") IapSysUserTDto iapSysUserTDto);
    List<IapSysUserTDto> pageQueryByState(@Param("iapSysUserTDto") IapSysUserTDto iapSysUserTDto);

    /**
     * 查询所有角色
     *
     * @return List<IapSysRoleTDto>
     */
    List<IapSysRoleTDto> queryAllRole();

    /**
     * 查询用户所属岗位
     *
     * @return List<IapSysOrganizationT>
     */
    List<IapSysOrganizationT> queryOrganizationById();

    /**
     * 通过用户 id 查询用户信息
     *
     * @return IapSysUserTDto
     */
    IapSysUserTDto queryUserById(@Param("iapSysUserTDto") IapSysUserTDto iapSysUserTDto);

    /**
     * 获取每页员工的用户
     *
     * @param iapSysUserTDto 用户实例
     * @return IPage<IapSysUserTDto>
     */
    IPage<IapSysUserTDto> getUserForEmp(Page<IapSysUserT> page, @Param("iapSysUserTDto") IapSysUserTDto iapSysUserTDto);

    /**
     * 获取用户以及关联岗位
     *
     * @param username 用户名
     * @return List<IapSysUserTDto>
     */
    List<IapSysUserTDto> queryUserInfoByUserName(@Param("username") String username);

    /**
     * 通过群ID查询群成员的信息
     *
     * @param groupId 群id
     * @return List<IapSysUserTDto>
     */
    List<IapSysUserTDto> getUserByGroupId(@Param("groupId") String groupId);


    /**
     * 查询拥有某个角色的用户
     * @param page
     * @param iapRoleUserQueryDTO
     * @return
     */
    IPage<IapSysUserT> getRoleUserList(Page<IapSysUserT> page, @Param("iapRoleUserQueryDTO")IapRoleUserQueryDTO iapRoleUserQueryDTO);


    /**
     * 查询未拥有某个角色的用户
     * @param page
     * @param iapRoleUserQueryDTO
     * @return
     */
    IPage<IapSysUserT> getNotRoleUserList(Page<IapSysUserT> page, @Param("iapRoleUserQueryDTO")IapRoleUserQueryDTO iapRoleUserQueryDTO);

}