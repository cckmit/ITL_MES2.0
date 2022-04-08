package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.entity.*;
import com.itl.iap.system.provider.mapper.*;
import org.springframework.stereotype.Service;
import com.itl.iap.system.api.dto.IapSysRoleTDto;
import com.itl.iap.system.api.service.IapSysRoleTService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 角色实现类
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Service
public class IapSysRoleTServiceImpl extends ServiceImpl<IapSysRoleTMapper, IapSysRoleT> implements IapSysRoleTService {

    @Resource
    private IapSysRoleTMapper iapSysRoleMapper;
    @Resource
    private IapSysUserRoleTMapper iapSysUserRoleMapper;
    @Resource
    private IapSysRoleAuthTMapper iapSysRoleAuthMapper;

    /**
     * 分页查询
     *
     * @param iapSysRoleDto 角色实例
     * @return IPage<IapSysRoleTDto>
     */
    @Override
    public IPage<IapSysRoleTDto> pageQuery(IapSysRoleTDto iapSysRoleDto) {
        if (null == iapSysRoleDto.getPage()) {
            iapSysRoleDto.setPage(new Page(0, 10));
        }
        return iapSysRoleMapper.pageQuery(iapSysRoleDto.getPage(), iapSysRoleDto);
    }
    @Override
    public IPage<IapSysRoleTDto> pageQueryByState(IapSysRoleTDto iapSysRoleDto) {
        if (null == iapSysRoleDto.getPage()) {
            iapSysRoleDto.setPage(new Page(0, 10));
        }
        return iapSysRoleMapper.pageQueryByState(iapSysRoleDto.getPage(), iapSysRoleDto);
    }

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRoleById(String id) {
        // 1 先删除用户和角色的中间表
        iapSysUserRoleMapper.delete(new QueryWrapper<IapSysUserRoleT>().eq("role_id", id));
        // 2 再删除角色和权限的中间表
        iapSysRoleAuthMapper.delete(new QueryWrapper<IapSysRoleAuthT>().eq("role_id", id));
        // 3 最后删除角色表
        return iapSysRoleMapper.delete(new QueryWrapper<IapSysRoleT>().eq("id", id)) > 0 ? true : false;
    }

    /**
     * 通过角色id查询用户和所属组织岗位
     *
     * @param iapSysRoleDto 角色实例
     * @return IPage<IapSysUserTDto>
     */
    @Override
    public IPage<IapSysUserTDto> queryUserListByRoleId(IapSysRoleTDto iapSysRoleDto) {
        if (null == iapSysRoleDto.getPage()) {
            iapSysRoleDto.setPage(new Page(0, 10));
        }
        return iapSysRoleMapper.selectUserListByRoleId(iapSysRoleDto.getPage(), iapSysRoleDto);
    }

    /**
     * 新建角色
     *
     * @param iapSysRoleT 角色实例
     * @return boolean
     * @throws CommonException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(IapSysRoleT iapSysRoleT) throws CommonException {
        if (iapSysRoleMapper.selectCount(new QueryWrapper<IapSysRoleT>().eq("role_code", iapSysRoleT.getRoleCode())) > 0) {
            throw new CommonException("该角色编码已存在，请重新输入", 500);
        }
        iapSysRoleT.setId(UUID.uuid32()).setCreateDate(new Date());
        if (iapSysRoleMapper.insert(iapSysRoleT) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改角色
     *
     * @param iapSysRoleT 角色实例
     * @return boolean
     * @throws CommonException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(IapSysRoleT iapSysRoleT) throws CommonException {
        // 通过新角色名称查询数据库中存在的角色
        IapSysRoleT sysRoleInDb = iapSysRoleMapper.selectOne(new QueryWrapper<IapSysRoleT>().eq("role_code", iapSysRoleT.getRoleCode()));
        if (sysRoleInDb != null) {
            // 判断更新的角色编码是不是原来那个
            if (!iapSysRoleT.getId().equals(sysRoleInDb.getId())) {
                throw new CommonException("新输入的角色编码已存在，请重新输入", 500);
            }
        }
        iapSysRoleT.setLastUpdateDate(new Date());
        return this.updateById(iapSysRoleT);
    }

    /**
     * 通过角色id查询用户和所属组织岗位
     *
     * @param roleId 分页
     * @return List<IapSysUserTDto>
     */
    @Override
    public List<IapSysUserTDto> queryAllUserListByRoleId(String roleId) {
        return iapSysRoleMapper.queryAllUserListByRoleId(roleId);
    }

    /**
     * 批量删除角色
     *
     * @param sysRoleList 角色列表
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<IapSysRoleT> sysRoleList) {
        if (!sysRoleList.isEmpty()) {
            // 1 先删除用户和角色的中间表
            iapSysUserRoleMapper.deleteBatchByRoleIdList(sysRoleList);
            // 2 再删除角色和权限的中间表
            iapSysRoleAuthMapper.deleteBatchByRoleIdList(sysRoleList);
            // 3 最后删除角色表
            return iapSysRoleMapper.deleteBatchByRoleIdList(sysRoleList);
        }
        return false;
    }

}
