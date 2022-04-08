package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.util.CodeUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysAuthTDto;
import com.itl.iap.system.api.entity.IapSysAuthT;
import com.itl.iap.system.api.entity.IapSysRoleAuthT;
import com.itl.iap.system.api.service.IapSysAuthTService;
import com.itl.iap.system.provider.mapper.IapSysAuthResourceTMapper;
import com.itl.iap.system.provider.mapper.IapSysAuthTMapper;
import com.itl.iap.system.provider.mapper.IapSysRoleAuthTMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 权限表实现类
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Service
public class IapSysAuthTServiceImpl extends ServiceImpl<IapSysAuthTMapper, IapSysAuthT> implements IapSysAuthTService {

    @Resource
    private IapSysAuthTMapper iapSysAuthMapper;
    @Resource
    private IapSysRoleAuthTMapper iapSysRoleAuthMapper;
    @Resource
    private IapSysAuthResourceTMapper sysAuthResourceMapper;

    /**
     * 分页查询
     *
     * @param iapSysAuthDto 权限对象
     * @return 权限分页对象
     */
    @Override
    public IPage<IapSysAuthTDto> pageQuery(IapSysAuthTDto iapSysAuthDto) {
        if (null == iapSysAuthDto.getPage()) {
            iapSysAuthDto.setPage(new Page(0, 10));
        }
        return iapSysAuthMapper.pageQuery(iapSysAuthDto.getPage(), iapSysAuthDto);
    }

    /**
     * 模糊查询权限
     *
     * @param iapSysAuthDto 权限对象
     * @return 权限集合
     */
    @Override
    public List<IapSysAuthTDto> selectTree(IapSysAuthTDto iapSysAuthDto) {

        return iapSysAuthMapper.selectTree(iapSysAuthDto);
    }

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeTreeById(String id) {
        List<String> idList = Lists.newArrayList();
        idList.add(id);
        // 递归找子id
        findAllId(id, idList);
        // 1 先删除角色和权限中间表
        iapSysRoleAuthMapper.delete(new QueryWrapper<IapSysRoleAuthT>().in("auth_id", idList));
        // 2 再删除权限和资源（菜单和按钮）中间表

        // 3 最后删除权限表
        return iapSysAuthMapper.delete(new QueryWrapper<IapSysAuthT>().in("id", idList)) > 0 ? true : false;
    }

    /**
     * 添加权限
     *
     * @param iapSysAuthT 权限对象
     * @return 是否添加成功
     * @throws CommonException
     */
    @Override
    public boolean addAuth(IapSysAuthT iapSysAuthT) throws CommonException {
        Date date = new Date();
        if (null == iapSysAuthT.getAuthCode() || "".equals(iapSysAuthT.getAuthCode())) {
            setCode(iapSysAuthT, date, 0);
        }
        if (iapSysAuthT.getId() == null) {
            iapSysAuthT.setId(UUID.uuid32());
        }
        return this.saveOrUpdate(iapSysAuthT);
    }

    /**
     * 修改权限
     *
     * @param iapSysAuthT 权限对象
     * @return 是否修改成功
     * @throws CommonException
     */
    @Override
    public boolean updateAuth(IapSysAuthT iapSysAuthT) throws CommonException {
        Date date = new Date();
        iapSysAuthT.setLastUpdateDate(date);
        IapSysAuthT sysAuthInDb = iapSysAuthMapper.selectOne(new QueryWrapper<IapSysAuthT>().eq("id", iapSysAuthT.getId()));
        // 判断权限是否重复
        if (!iapSysAuthT.getAuthCode().equals(sysAuthInDb.getAuthCode())) {
            Integer authCodeNum = iapSysAuthMapper.selectCount(new QueryWrapper<IapSysAuthT>().ne("id", iapSysAuthT.getId()).eq("auth_code", iapSysAuthT.getAuthCode()));
            if (authCodeNum != 0) {
                throw new CommonException(new CommonExceptionDefinition(500, "权限编码重复!"));
            }
        }
        // 判断前端是否修改权限状态
        if (iapSysAuthT.getState() != null && iapSysAuthT.getState().equals(sysAuthInDb.getState())) {
            return this.updateById(iapSysAuthT);
        }
        // 前端修改了权限状态，应该修改其状态和子集状态
        // 查找权限是否有子集
        List<IapSysAuthT> childrenList = iapSysAuthMapper.selectList(new QueryWrapper<IapSysAuthT>().eq("parent_id", iapSysAuthT.getId()));
        // 没有子权限，直接更新
        if (childrenList.size() == 0) {
            return this.updateById(iapSysAuthT);
        }
        // 递归获取子权限
        Set<IapSysAuthT> set = new HashSet<>();
        for (IapSysAuthT sysAuthT : childrenList) {
            this.getChildrenAuth(sysAuthT, set);
        }
        childrenList.addAll(set);
        // 修改所有子权限的状态，跟父权限一致
        for (IapSysAuthT sysAuthT : childrenList) {
            sysAuthT.setState(iapSysAuthT.getState());
            iapSysAuthMapper.updateById(sysAuthT);
        }
        return this.updateById(iapSysAuthT);
    }

    /**
     * 批量删除权限
     *
     * @param sysAuthList 要删除的权限集合
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBatch(List<IapSysAuthT> sysAuthList) {
        if (sysAuthList != null && !sysAuthList.isEmpty()) {
            // 1 先删除角色和权限中间表
            iapSysRoleAuthMapper.deleteBatchByAuthIdList(sysAuthList);
            // 2 再删除权限和资源（菜单和按钮）中间表
            sysAuthResourceMapper.deleteBatchByAuthIdList(sysAuthList);
            // 3 最后删除权限表
            return iapSysAuthMapper.deleteList(sysAuthList);
        }
        return false;
    }

    /**
     * 查询权限树
     *
     * @param iapSysAuthDto 权限对象
     * @return 权限树
     */
    @Override
    public List<IapSysAuthTDto> getTree(IapSysAuthTDto iapSysAuthDto) {
        // 先通过前端传入的条件查询
        Set<IapSysAuthTDto> parentAuthSet = new HashSet<>(iapSysAuthMapper.selectTree(iapSysAuthDto));
        if (parentAuthSet.isEmpty()) {
            return new ArrayList<>(parentAuthSet);
        }
        // 全部权限列表
        List<IapSysAuthTDto> allAuthDtoList = iapSysAuthMapper.selectTree(new IapSysAuthTDto());
        // 创建一个子权限的Set
        Set<IapSysAuthTDto> childrentAuthSet = new HashSet<>(allAuthDtoList.size() * 2);
        // 查询父权限下的子权限
        parentAuthSet.forEach(parentAuth -> this.getChildrenByParentAuthId(parentAuth, childrentAuthSet, allAuthDtoList));
        // 排除掉父权限与子权限重复部分，防止树状结构出现重复
        parentAuthSet.removeAll(childrentAuthSet);
        // 把父组织set和子组织set重组为树形结构，返回给前端
        parentAuthSet.forEach(parentAuth -> this.setAuthTreeByParent(parentAuth, new ArrayList<>(childrentAuthSet)));
        return parentAuthSet.stream().sorted(Comparator.comparing(IapSysAuthTDto::getCreateDate).reversed()).collect(Collectors.toList());
    }
   /**
     * 查询权限树
     *
     * @param iapSysAuthDto 权限对象
     * @return 权限树
     */
    @Override
    public List<IapSysAuthTDto> getTreeByState(IapSysAuthTDto iapSysAuthDto) {
        // 先通过前端传入的条件查询
        Set<IapSysAuthTDto> parentAuthSet = new HashSet<>(iapSysAuthMapper.selectTree(iapSysAuthDto));
        if (parentAuthSet.isEmpty()) {
            return new ArrayList<>(parentAuthSet);
        }
        // 全部权限列表
        List<IapSysAuthTDto> allAuthDtoList = iapSysAuthMapper.selectTreeByState(new IapSysAuthTDto());
        // 创建一个子权限的Set
        Set<IapSysAuthTDto> childrentAuthSet = new HashSet<>(allAuthDtoList.size() * 2);
        // 查询父权限下的子权限
        parentAuthSet.forEach(parentAuth -> this.getChildrenByParentAuthId(parentAuth, childrentAuthSet, allAuthDtoList));
        // 排除掉父权限与子权限重复部分，防止树状结构出现重复
        parentAuthSet.removeAll(childrentAuthSet);
        // 把父组织set和子组织set重组为树形结构，返回给前端
        parentAuthSet.forEach(parentAuth -> this.setAuthTreeByParent(parentAuth, new ArrayList<>(childrentAuthSet)));
        return parentAuthSet.stream().sorted(Comparator.comparing(IapSysAuthTDto::getCreateDate).reversed()).collect(Collectors.toList());
    }

    /**
     * 根据父ID递归查询子权限
     *
     * @param parentSysAuthDto      父权限
     * @param childrenSysAuthDtoSet 子权限
     * @param allSysAuthDtoSet      所有权限
     */
    private void getChildrenByParentAuthId(IapSysAuthTDto parentSysAuthDto, Set<IapSysAuthTDto> childrenSysAuthDtoSet,
                                           List<IapSysAuthTDto> allSysAuthDtoSet) {
        // 筛选出 parentOrganization 的子组织
        Set<IapSysAuthTDto> collect = allSysAuthDtoSet.stream().filter(x -> x.getParentId().equals(parentSysAuthDto.getId())).collect(Collectors.toSet());
        if (!collect.isEmpty()) {
            childrenSysAuthDtoSet.addAll(collect);
            collect.stream().forEach(x -> this.getChildrenByParentAuthId(x, childrenSysAuthDtoSet, allSysAuthDtoSet));
        }
    }

    /**
     * 递归拼接树形结构
     *
     * @param dto                     需要拼接父级节点
     * @param parentIapSysAuthDtoList 子级节点
     */
    public void setAuthTreeByParent(IapSysAuthTDto dto, List<IapSysAuthTDto> parentIapSysAuthDtoList) {
        List<IapSysAuthTDto> codeTreeList = new ArrayList<>();
        parentIapSysAuthDtoList.forEach(x -> {
            if (x.getParentId() != null && x.getParentId().equals(dto.getId())) {
                codeTreeList.add(x);
                setAuthTreeByParent(x, parentIapSysAuthDtoList);
            }
        });
        if (codeTreeList.size() > 0) {
            dto.setChildren(codeTreeList);
        }
    }

    /**
     * @param iapSysAuthT 父级权限
     * @param set         子级权限集合
     */
    private void getChildrenAuth(IapSysAuthT iapSysAuthT, Set<IapSysAuthT> set) {
        List<IapSysAuthT> childrenList = iapSysAuthMapper.selectList(new QueryWrapper<IapSysAuthT>().eq("parent_id", iapSysAuthT.getId()));
        set.addAll(childrenList);
        for (IapSysAuthT sysAuthT : childrenList) {
            this.getChildrenAuth(sysAuthT, set);
        }
    }

    /**
     * lh
     * 2020/6/21 16:22
     * 递归查询子id，并拼接ids
     *
     * @param id     子id
     * @param idList 子id集合
     */
    private void findAllId(String id, List<String> idList) {
        List<IapSysAuthT> auth = iapSysAuthMapper.selectList(new QueryWrapper<IapSysAuthT>().eq("parent_id", id));
        if (auth.size() <= 0) {
            return;
        }
        for (IapSysAuthT iapSysAuthT : auth) {
            String findId = iapSysAuthT.getId();
            idList.add(findId);
            findAllId(findId, idList);
        }
    }

    /**
     * @param obj  需要设置code属性的实体类
     * @param date 当前时间
     * @param num  默认0 大于 20跳出
     */
    private void setCode(IapSysAuthT obj, Date date, Integer num) throws CommonException {
        obj.setAuthCode(CodeUtils.dateToCode("QX", date));
        if (num > CodeUtils.NUM) {
            throw new CommonException(new CommonExceptionDefinition(500, "编码设置失败!"));
        }
        if (iapSysAuthMapper.selectCount(new QueryWrapper<IapSysAuthT>().ne("id", obj.getId()).eq("auth_code", obj.getAuthCode())) != 0) {
            num++;
            this.setCode(obj, date, num);
        }
    }

}

