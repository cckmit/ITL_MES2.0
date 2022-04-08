package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapPositionTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.entity.IapPositionT;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗位mapper
 *
 * @author 马家伦
 * @date 2020-06-16
 * @since jdk1.8
 */
public interface IapSysPositionTMapper extends BaseMapper<IapPositionT> {
    /**
     * 通过职位编码和职位名称查询
     *
     * @param iapPositionTDto 岗位对象
     * @return IPage<IapPositionTDto>
     */
    IPage<IapPositionTDto> queryList(Page page, @Param("iapPositionTDto") IapPositionTDto iapPositionTDto);

    /**
     * 更新岗位状态(enabled)
     *
     * @param iapPositionTDtoList 岗位集合
     * @param enabled 启用状态
     * @return Boolean
     */
    Boolean updateEnabled(@Param("iapPositionTDtoList") List<IapPositionTDto> iapPositionTDtoList, @Param("enabled") Short enabled);

    /**
     * 通过 岗位 id 查询岗位下的用户数量
     *
     * @param iapPositionT 岗位对象
     * @return IapPositionTDto
     */
    IapPositionTDto queryUserNumByPositionId(@Param("iapPositionT") IapPositionT iapPositionT);

    /**
     * 通过岗位 id 查询用户列表
     *
     * @param iapPositionT 岗位对象
     * @return IPage<IapSysUserTDto>
     */
    IPage<IapSysUserTDto> queryUserListByPositionId(Page page, @Param("iapPositionT") IapPositionT iapPositionT);

    /**
     * 通过岗位 id 查询用户列表
     *
     * @param id 岗位id
     * @return List<IapSysUserTDto>
     */
    List<IapSysUserTDto> userListByPositionId(@Param("id") String id);

    /**
     * 删除岗位-员工中间表信息(iap_sys_position_employee_T)信息
     *
     * @param iapPositionT 岗位对象
     * @return Boolean
     */
    Boolean deletePosititionEmpployeeTByPositionId(@Param("iapPositionT") IapPositionT iapPositionT);

    /**
     * 删除岗位-组织中间表信息(iap_sys_organization_position_t)信息
     *
     * @param iapPositionT 岗位对象
     * @return Boolean
     */
    Boolean deleteOrganizationPositionTByPositionId(@Param("iapPositionT") IapPositionT iapPositionT);


    /**
     * 通过 岗位id 查询组织信息
     *
     * @param parentId 岗位id
     * @return IapPositionTDto
     */
    IapPositionTDto getOrganizationByPositionId(@Param("parentId") String parentId);

    /**
     * 更新 岗位-组织 中间表
     * @param iapPositionTDto 岗位对象
     * @return Integer
     */
    Integer updateOrganizationPosition(@Param("iapPositionTDto") IapPositionTDto iapPositionTDto);

    /**
     * 更新岗位信息
     * @param iapPositionTDto
     * @return Integer
     */
    Integer updateOneById(@Param("iapPositionTDto") IapPositionT iapPositionTDto);

    /**
     * 首页树状岗位列表
     *
     * @param iapPositionTDto 岗位对象
     * @return List<IapPositionTDto>
     */
    List<IapPositionTDto> getListTree(@Param("iapPositionTDto") IapPositionTDto iapPositionTDto);

    /**
     * 通过组织id查看岗位列表
     *
     * @param iapPositionTDto 岗位对象
     * @return List<IapPositionTDto>
     */
    List<IapPositionTDto> getListTreeByOrgId(@Param("iapPositionTDto") IapPositionTDto iapPositionTDto);


    /**
     * IM获取岗位与用户的信息
     * @param positName 岗位名称
     * @param enabled 启用状态
     * @return List<IapPositionTDto>
     */
    List<IapPositionTDto> getPositUser(@Param("positName")String positName,@Param("enabled") short enabled);
}

