package com.itl.iap.system.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.api.dto.IapSysRoleTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.dto.req.IapRoleUserQueryDTO;
import com.itl.iap.system.api.dto.req.IapRoleUserSaveDTO;
import com.itl.iap.system.api.dto.req.IapSysUserProveSaveDTO;
import com.itl.iap.system.api.dto.req.IapSysUserStationSaveDTO;
import com.itl.iap.system.api.entity.IapSysPositionUserT;
import com.itl.iap.system.api.entity.IapSysUserT;

import java.util.List;

/**
 * 用户表Service
 *
 * @author 谭强
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysUserTService extends IService<IapSysUserT> {

    /**
     * 分页查询
     *
     * @param iapSysUserDto 用户实例
     * @return IPage<IapSysUserTDto>
     */
    IPage<IapSysUserTDto> pageQuery(IapSysUserTDto iapSysUserDto);
    IPage<IapSysUserTDto> pageQueryByState(IapSysUserTDto iapSysUserDto);

    /**
     * 新增用户
     *
     * @param iapSysUserT 用户实例
     * @return Boolean
     */
    Boolean installOneUser(IapSysUserT iapSysUserT);

    /**
     * 修改用户
     *
     * @param iapSysUserT 用户实例
     * @return Boolean
     */
    Boolean updateOneUserById(IapSysUserT iapSysUserT);

    /**
     * 根据用户id集合删除用户
     *
     * @param ids 用户id列表
     * @return Boolean
     */
    Boolean removeUserByIds(List<String> ids);

    /**
     * 根据单个Id删除
     *
     * @param id 用户id
     * @return Boolean
     */
    Boolean removeUserByOneId(String id);

    /**
     * 查询所有角色
     *
     * @return List<IapSysRoleTDto>
     */
    List<IapSysRoleTDto> queryAllRole();

    /**
     * 修改用户的岗位
     *
     * @param iapSysPositionUserT 用户岗位实例
     * @return Boolean
     */
    Boolean savePositionIdAndUserId(List<IapSysPositionUserT> iapSysPositionUserT, String userId);

    /**
     * 根据用户名查询个人信息
     *
     * @param userName 用户姓名
     * @return IapSysUserTDto
     */
    IapSysUserTDto queryByUserName(String userName) throws CommonException;

    /**
     * 通过用户 id 查询用户信息
     *
     * @return IapSysUserTDto
     */
    IapSysUserTDto queryUserById();

    /**
     * 通过用户 id 查询用户信息
     *
     * @return IapSysUserT
     */
    IapSysUserT queryUserId(String userId);

    /**
     * 根据用户id用户密码
     *
     * @param iapSysUserDto 用户实例
     * @return Integer
     */
    Integer updateUserPwdById(IapSysUserTDto iapSysUserDto);

    /**
     * 获取每页员工的用户
     *
     * @param iapSysUserDto 用户实例
     * @return IPage<IapSysUserTDto>
     */
    IPage<IapSysUserTDto> getUserForEmp(IapSysUserTDto iapSysUserDto);

    /**
     * 方法功能描述：根据用户名称模糊查询用户信息
     *
     * @param username 用户姓名
     * @return List<IapSysUserTDto>
     */
    List<IapSysUserTDto> queryUserInfoByUserName(String username);

    /**
     * 通过聊天群ID查找群成员
     *
     * @param groupId 群id
     * @return List<IapSysUserTDto>
     */
    List<IapSysUserTDto> getUserByGroupId(String groupId);

    /**
     * 根据用户名称精确查询用户信息
     *
     * @param username 用户姓名
     * @return IapSysUserTDto
     */
    IapSysUserTDto preciseQueryUserInformation(String username);



    IPage<IapSysUserT> getRoleUserList(IapRoleUserQueryDTO iapRoleUserQueryDTO);


    IPage<IapSysUserT> getNotRoleUserList(IapRoleUserQueryDTO iapRoleUserQueryDTO);


    /**
     * 为用户分配角色
     * @param iapRoleUserSaveDTO
     */
    void saveRoleUsers(IapRoleUserSaveDTO iapRoleUserSaveDTO);

    /**
     * 为用户分配证明
     * @param iapSysUserProveSaveDTO
     */
    void saveUserProves(IapSysUserProveSaveDTO iapSysUserProveSaveDTO);

    /**
     * 为用户分配工位
     * @param iapSysUserStationSaveDTO
     */
    void saveUserStations(IapSysUserStationSaveDTO iapSysUserStationSaveDTO);
}