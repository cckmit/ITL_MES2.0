package com.itl.iap.system.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.api.dto.IapSysAuthTDto;
import com.itl.iap.system.api.entity.IapSysAuthT;

import java.util.List;

/**
 * 权限表service
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysAuthTService extends IService<IapSysAuthT> {

    /**
     * 分页查询
     *
     * @param iapSysAuthDto  权限对象
     * @return 权限分页对象
     */
    IPage<IapSysAuthTDto> pageQuery(IapSysAuthTDto iapSysAuthDto);

    /**
     * 模糊查询权限
     *
     * @param iapSysAuthTDto 权限对象
     * @return 权限集合
     */
    List<IapSysAuthTDto> selectTree(IapSysAuthTDto iapSysAuthDto);

    /**
     * lh
     * 2020/6/21 15:59
     * 根据当前id，向下查找所有id，批量删除
     * 1 先删除角色和权限中间表
     * 2 再删除权限和资源（菜单和按钮）中间表
     * 3 最后删除权限表
     *
     * @param id 权限id
     * @return 是否删除成功
     */
    boolean removeTreeById(String id);

    /**
     * 添加权限
     *
     * @param iapSysAuthT 权限对象
     * @return 是否添加成功
     * @throws CommonException
     */
    boolean addAuth(IapSysAuthT iapSysAuthT) throws CommonException;

    /**
     * 修改权限
     *
     * @param iapSysAuthT 权限对象
     * @return 是否修改成功
     * @throws CommonException
     */
    boolean updateAuth(IapSysAuthT iapSysAuthT) throws CommonException;

    /**
     * 批量删除权限
     *
     * @param sysAuthList 要删除的权限集合
     * @return 是否删除成功
     */
    boolean deleteBatch(List<IapSysAuthT> sysAuthList);

    /**
     * 查询权限树
     *
     * @param iapSysAuthDto 权限对象
     * @return 权限树
     */
    List<IapSysAuthTDto> getTree(IapSysAuthTDto iapSysAuthDto);
    List<IapSysAuthTDto> getTreeByState(IapSysAuthTDto iapSysAuthDto);


}
