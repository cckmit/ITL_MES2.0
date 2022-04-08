package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.api.dto.IapPositionTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.entity.IapPositionT;

import java.util.List;

/**
 * 岗位service
 *
 * @author 马家伦
 * @date 2020-06-16
 * @since jdk1.8
 */
public interface IapPositionTService extends IService<IapPositionT> {
    /**
     * 添加岗位
     *
     * @param iapPositionDto 岗位对象
     * @return String
     */
    String add(IapPositionTDto iapPositionDto) throws CommonException;

    /**
     * 更新岗位
     *
     * @param iapPositionDto 岗位对象
     * @return Boolean
     */
    Boolean update(IapPositionTDto iapPositionDto) throws CommonException;

    /**
     * 通过职位编码和职位名称查询
     *
     * @param iapPositionDto 岗位对象
     * @return IPage<IapPositionTDto>
     */
    IPage<IapPositionTDto> query(IapPositionTDto iapPositionDto);

    /**
     * 更新岗位状态(enabled)
     *
     * @param iapPositionDto 岗位对象
     * @param enabled         启用状态
     * @return Boolean
     */
    Boolean updateEnabled(IapPositionTDto iapPositionDto, Short enabled);

    /**
     * 通过岗位id查询用户列表
     *
     * @param iapPositionDto 岗位对象
     * @return IPage<IapSysUserTDto>
     */
    IPage<IapSysUserTDto> queryUserListByPositionId(IapPositionTDto iapPositionDto);

    /**
     * 通过岗位id删除
     *
     * @param iapPositionDto 岗位对象
     * @return Boolean
     */
    Boolean deleteById(IapPositionTDto iapPositionDto);

    /**
     * 首页树状岗位列表
     *
     * @param iapPositionDto 岗位对象
     * @return IPage<IapPositionTDto>
     */
    IPage<IapPositionTDto> getListTree(IapPositionTDto iapPositionDto);

    /**
     * 通过组织id查看岗位列表
     *
     * @param iapPositionDto 岗位对象
     * @return List<IapPositionTDto>
     */
    List<IapPositionTDto> getListTreeByOrgId(IapPositionTDto iapPositionDto);

    /**
     * 通过岗位id查看用户列表
     *
     * @param positionId 父id
     * @return List<IapSysUserTDto>
     */
    List<IapSysUserTDto> queryUserListByPositionId(String positionId);

    /**
     * 批量删除岗位
     *
     * @param positionDtoList 岗位集合
     * @return Boolean
     */
    Boolean deleteBatch(List<IapPositionTDto> positionDtoList);

    /**
     * 岗位树
     *
     * @param iapPositionDto 岗位对象
     * @return List<IapPositionTDto>
     */
    List<IapPositionTDto> getTree(IapPositionTDto iapPositionDto);
}

