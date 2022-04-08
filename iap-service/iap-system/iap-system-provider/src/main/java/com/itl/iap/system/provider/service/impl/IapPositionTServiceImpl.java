package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.util.CodeUtils;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.common.util.PageUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapPositionTDto;
import com.itl.iap.system.api.dto.IapSysOrganizationTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.entity.IapPositionT;
import com.itl.iap.system.api.entity.IapSysOrganizationPositionT;
import com.itl.iap.system.provider.mapper.IapSysPositionTMapper;
import com.itl.iap.system.provider.mapper.IapSysOrganizationPositionTMapper;
import com.itl.iap.system.api.service.IapPositionTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

/**
 * 岗位实现类
 *
 * @author 马家伦
 * @date 2020-06-16
 * @since jdk1.8
 */
@Service
@Slf4j
public class IapPositionTServiceImpl extends ServiceImpl<IapSysPositionTMapper, IapPositionT> implements IapPositionTService {

    @Resource
    IapSysPositionTMapper iapSysPositionMapper;

    @Resource
    IapSysOrganizationPositionTMapper iapSysOrganizationPositionMapper;

    /**
     * 添加岗位
     *
     * @param iapPositionDto 岗位对象
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String add(@Valid IapPositionTDto iapPositionDto) throws CommonException {
        if (iapPositionDto.getParentId() == null && iapPositionDto.getOrgId() == null) {
            throw new CommonException("请选择部门", 500);
        }
        Date date = new Date();
        IapPositionT iapPositionT = DtoUtils.convertObj(iapPositionDto, IapPositionT.class);
        iapPositionT.setId(UUID.uuid32()).setEnabled((short) 0).setCreateDate(date).setLastUpdateDate(date);

        try {
            this.setCode(iapPositionT, date, 0);
        } catch (CommonException e) {
            e.printStackTrace();
        }

        // 前端传入的父岗位为空字符串, 把空字符串置空
        if (iapPositionDto.getParentId().isEmpty()) {
            iapPositionDto.setParentId(null);
            iapPositionT.setParentId(null);
        }

        // 插入 iap_sys_position_t 表
        iapSysPositionMapper.insert(iapPositionT);

        // 前端传入的组织不为空，首先判断的是否有上级岗位，如果存在上级岗位，则查询上级岗位的所属部门，如果不存在上级岗位，则直接插入前端传入的部门
        if (iapPositionDto.getParentId() != null) {
            // 前端传入有上级岗位
            IapSysOrganizationPositionT iapSysOrganizationPositionT = iapSysOrganizationPositionMapper.selectOne(new QueryWrapper<IapSysOrganizationPositionT>().eq("position_id", iapPositionDto.getParentId()));
            // 判断上级岗位是否有所属组织
            if (iapSysOrganizationPositionT != null) {
                // 上级岗位有所属组织
                IapSysOrganizationPositionT newIapSysOrganizationPositionT = new IapSysOrganizationPositionT();
                newIapSysOrganizationPositionT.setId(UUID.uuid32()).setPositionId(iapPositionT.getId()).
                        setOrganizationId(iapSysOrganizationPositionT.getOrganizationId());
                iapSysOrganizationPositionMapper.insert(newIapSysOrganizationPositionT);
                return iapPositionT.getId();
            } else {
                // 上级岗位没有所属组织，则当前岗位无法插入所属组织
                if (iapPositionDto.getOrgId() != null) {
                    throw new RuntimeException("当前岗位的上级岗位没有绑定组织，所以当前岗位无法绑定组织");
                }
            }
        }

        // 没有父级岗位，直接插入组织
        if (iapPositionDto.getOrgId() != null) {
            IapSysOrganizationPositionT newIapSysOrganizationPositionT = new IapSysOrganizationPositionT();
            newIapSysOrganizationPositionT.setId(UUID.uuid32()).setPositionId(iapPositionT.getId()).
                    setOrganizationId(iapPositionDto.getOrgId());
            iapSysOrganizationPositionMapper.insert(newIapSysOrganizationPositionT);
            return iapPositionT.getId();
        }

        return iapPositionT.getId();
    }

    /**
     * 更新岗位
     *
     * @param iapPositionDto 岗位对象
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean update(IapPositionTDto iapPositionDto) throws CommonException {
        // 根据前端传入的部门id的值，获取数据库中对应的组织id
        IapSysOrganizationPositionT iapSysOrganizationPositionT = iapSysOrganizationPositionMapper.selectOne(new QueryWrapper<IapSysOrganizationPositionT>().eq("position_id", iapPositionDto.getId()));
        IapPositionT iapPositionTinDb = iapSysPositionMapper.selectOne(new QueryWrapper<IapPositionT>().eq("id", iapPositionDto.getId()));

        // 该岗位无需修改组织
        if (iapSysOrganizationPositionT.getOrganizationId().equals(iapPositionDto.getOrgId())) {
            // 该岗位只需修改基础信息
            if ((iapPositionTinDb.getParentId() == null && iapPositionDto.getParentId() == null)
                    || (iapPositionTinDb.getParentId() != null && iapPositionDto.getParentId() != null && iapPositionTinDb.getParentId().equals(iapPositionDto.getParentId()))) {
                iapSysPositionMapper.updateOneById(DtoUtils.convertObj(iapPositionDto, IapPositionT.class));
                return true;
            }
        }

        // 1. 前端传的，没有部门id  并且查数据库该岗位也没有部门 则提示该岗位未选择部门不可以修改
        if (iapPositionDto.getOrgId() == null && iapSysOrganizationPositionT != null && iapSysOrganizationPositionT.getOrganizationId() == null) {
            throw new CommonException("没有选择部门", 500);
        }
        // 查询当前岗位是否存在子岗位，如果存在则不能修改
//        Integer parentIdCount = iapSysPositionTMapper.selectCount(new QueryWrapper<IapPositionT>().eq("parent_id", iapPositionTDto.getId()));
//        if (parentIdCount != 0) {
//            throw new CommonException("该岗位存在子岗位，不可修改", 500);
//        }
        IapPositionT iapPositionT = DtoUtils.convertObj(iapPositionDto, IapPositionT.class);
        // 修改部门
        if (iapSysOrganizationPositionT != null && iapSysOrganizationPositionT.getOrganizationId() != null && !iapSysOrganizationPositionT.getOrganizationId().equals(iapPositionDto.getOrgId())) {
            iapSysOrganizationPositionT.setOrganizationId(iapPositionDto.getOrgId());
            IapPositionT iapPositionT1 = iapSysPositionMapper.selectById(iapPositionDto.getId());
            // 判断传过来的父级id是否是原来的父级id
        /*    if (iapPositionTDto.getParentId() != null && iapPositionT1.getParentId() != null && iapPositionT1.getParentId().equals(iapPositionTDto.getParentId())) {
                iapPositionT.setParentId(null);
            }*/
            iapSysOrganizationPositionMapper.updateById(iapSysOrganizationPositionT);
        }
        // 部门不存在的情况，进行添加部门
        if (iapPositionDto.getOrgId() != null && iapSysOrganizationPositionT == null && iapSysOrganizationPositionT.getOrganizationId() != null) {
            IapSysOrganizationPositionT iapSysOrganizationPosition = new IapSysOrganizationPositionT();
            iapSysOrganizationPosition.setId(UUID.uuid32());
            iapSysOrganizationPosition.setOrganizationId(iapPositionDto.getOrgId());
            iapSysOrganizationPosition.setPositionId(iapPositionDto.getId());
            iapSysOrganizationPositionMapper.insert(iapSysOrganizationPosition);
        }
        iapSysPositionMapper.updateOneById(iapPositionT);
        return true;
    }

    /**
     * 通过职位编码和职位名称查询
     *
     * @param iapPositionDto 岗位对象
     * @return IPage<IapPositionTDto>
     */
    @Override
    public IPage<IapPositionTDto> query(IapPositionTDto iapPositionDto) {
        if (iapPositionDto.getPage() == null) {
            iapPositionDto.setPage(new Page(0, 10));
        }
        try {
            return iapSysPositionMapper.queryList(iapPositionDto.getPage(), iapPositionDto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新岗位状态(enabled)
     *
     * @param iapPositionDto 岗位对象
     * @param enabled        启用状态
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateEnabled(IapPositionTDto iapPositionDto, Short enabled) {
        IapPositionT iapPositionT = DtoUtils.convertObj(iapPositionDto, IapPositionT.class);
        IapPositionTDto iapPositionDtoInDb = iapSysPositionMapper.queryUserNumByPositionId(iapPositionT);
        // 查找该岗位下的用户数
        if (iapPositionDtoInDb.getUserNum() == 0) {
            iapPositionT.setEnabled(enabled);
            iapSysPositionMapper.updateById(iapPositionT);
            return true;
        } else {
            throw new RuntimeException("传入的岗位下面有员工,无法关闭该岗位");
        }

    }

    /**
     * 通过岗位id查询用户列表
     *
     * @param iapPositionDto 岗位对象
     * @return IPage<IapSysUserTDto>
     */
    @Override
    public IPage<IapSysUserTDto> queryUserListByPositionId(IapPositionTDto iapPositionDto) {
        if (iapPositionDto.getPage() == null) {
            iapPositionDto.setPage(new Page(0, 10));
        }
        try {
            return iapSysPositionMapper.queryUserListByPositionId(iapPositionDto.getPage(), DtoUtils.convertObj(iapPositionDto, IapPositionT.class));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过岗位id删除
     *
     * @param iapPositionDto 岗位对象
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteById(IapPositionTDto iapPositionDto) {
        IapPositionT iapPositionT = new IapPositionT();
        BeanUtils.copyProperties(iapPositionDto, iapPositionT);

        if (iapPositionDto.getId() != null) {
            // 判断删除的岗位是否是最低层岗位
            Integer childrenNum = iapSysPositionMapper.selectCount(new QueryWrapper<IapPositionT>().eq("parent_id", iapPositionDto.getId()));
            if (childrenNum != 0) {
                // 有子岗位
                throw new RuntimeException("不能直接删除该岗位，该岗位有：" + childrenNum + " 个子岗位，先修改子岗位");
            } else {
                // 删除 iap_sys_position_t 表
                iapSysPositionMapper.deleteById(iapPositionDto.getId());
                // 删除岗位-员工中间表信息(iap_sys_position_employee_T)信息
                iapSysPositionMapper.deletePosititionEmpployeeTByPositionId(iapPositionT);
                // 删除岗位-组织中间表信息(iap_sys_organization_position_t)信息
                iapSysPositionMapper.deleteOrganizationPositionTByPositionId(iapPositionT);
                return true;
            }
        }
        return false;
    }

    /**
     * 首页树状岗位列表
     *
     * @param iapPositionDto 岗位对象
     * @return IPage<IapPositionTDto>
     */
    @Override
    public IPage<IapPositionTDto> getListTree(IapPositionTDto iapPositionDto) {
        IPage<IapPositionTDto> iPage = new Page<>();
        if (iapPositionDto.getPage() == null) {
            iapPositionDto.setPage(new Page(1, 10));
        }
        int start = PageUtils.getStart(toIntExact(iapPositionDto.getPage().getCurrent()) - 1, toIntExact(iapPositionDto.getPage().getSize()));
        int end = PageUtils.getEnd(toIntExact(iapPositionDto.getPage().getCurrent() - 1), toIntExact(iapPositionDto.getPage().getSize()));
        List<IapPositionTDto> listTree = iapSysPositionMapper.getListTree(iapPositionDto);
        List<IapPositionTDto> collect = listTree.stream().filter(x -> x.getParentId() == null).sorted(Comparator.comparing(IapPositionTDto::getSort)).collect(Collectors.toList());
        if (collect.isEmpty()) {
            // listTree
            if (end > listTree.size()) {
                end = listTree.size();
            }
            if (start > end) {
                start = end;
            }
            List<IapPositionTDto> list = listTree.subList(start, end);
            iPage.setRecords(list).setTotal(listTree.size()).setCurrent(iapPositionDto.getPage().getCurrent()).setSize(iapPositionDto.getPage().getSize());
            return iPage;
        } else {
            collect.forEach(x -> {
                recursiveMenu(x, listTree);
            });
            // collect
            if (end > collect.size()) {
                end = collect.size();
            }
            if (start > end) {
                start = end;
            }
            List<IapPositionTDto> list = collect.subList(start, end);
            iPage.setRecords(list).setTotal(collect.size()).setCurrent(iapPositionDto.getPage().getCurrent()).setSize(iapPositionDto.getPage().getSize());
            return iPage;
        }
    }

    /**
     * 递归为树形结构
     *
     * @param list          父岗位
     * @param recursiveDtos 子岗位集合
     */
    private void recursiveMenu(IapPositionTDto list, List<IapPositionTDto> recursiveDtos) {
        List<IapPositionTDto> codeTreeList = new ArrayList<>();
        recursiveDtos.forEach(x -> {
            if (x.getParentId() != null && x.getParentId().equals(list.getId())) {
                codeTreeList.add(x);
                recursiveMenu(x, recursiveDtos);
            }
        });
        if (codeTreeList.size() > 0) {
//            codeTreeList.stream().filter(x -> x.getSort() != null).sorted(Comparator.comparing(IapPositionTDto::getSort)).collect(Collectors.toList());
            list.setPositionlist(codeTreeList);
        }
    }

    /**
     * 通过组织id查看岗位列表
     *
     * @param iapPositionDto 岗位对象
     * @return List<IapPositionTDto>
     */
    @Override
    public List<IapPositionTDto> getListTreeByOrgId(IapPositionTDto iapPositionDto) {
        List<IapPositionTDto> listTree = iapSysPositionMapper.getListTreeByOrgId(iapPositionDto);
        List<IapPositionTDto> collect = listTree.stream().filter(x -> x.getParentId() == null).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return listTree;
        } else {
            collect.forEach(x -> {
                recursiveMenu(x, listTree);
            });
            return collect;
        }
    }

    /**
     * 通过岗位id查看用户列表
     *
     * @param positionId 父id
     * @return List<IapSysUserTDto>
     */
    @Override
    public List<IapSysUserTDto> queryUserListByPositionId(String positionId) {
        return iapSysPositionMapper.userListByPositionId(positionId);
    }

    /**
     * 批量删除岗位
     *
     * @param positionDtoList 岗位集合
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteBatch(List<IapPositionTDto> positionDtoList) {
        // 根据需求，由前端判断是否有子岗位，后端无需判断
        if (!positionDtoList.isEmpty()) {
            for (IapPositionTDto iapPositionDto : positionDtoList) {
                IapPositionT iapPositionT = DtoUtils.convertObj(iapPositionDto, IapPositionT.class);
                // 删除 iap_sys_position_t 表
                iapSysPositionMapper.deleteById(iapPositionT.getId());
                // 删除岗位-员工中间表信息(iap_sys_position_employee_T)信息
                iapSysPositionMapper.deletePosititionEmpployeeTByPositionId(iapPositionT);
                // 删除岗位-组织中间表信息(iap_sys_organization_position_t)信息
                iapSysPositionMapper.deleteOrganizationPositionTByPositionId(iapPositionT);
            }
            return true;
        }
        return false;
    }

    /**
     * 岗位树
     *
     * @param iapPositionDto 岗位对象
     * @return List<IapPositionTDto>
     */
    @Override
    public List<IapPositionTDto> getTree(IapPositionTDto iapPositionDto) {

        // 先通过前端传入的条件查询
        Set<IapPositionTDto> parentPositionSet = new HashSet<>(iapSysPositionMapper.getListTree(iapPositionDto));
        if (parentPositionSet.isEmpty()){
            return new ArrayList<>(parentPositionSet);
        }
        // 全部岗位列表
        List<IapPositionTDto> allPositionSet = DtoUtils.convertList(IapPositionTDto.class, iapSysPositionMapper.getListTree(new IapPositionTDto()));
        // 创建一个子组织set
        Set<IapPositionTDto> childrenPositionSet = new HashSet<>(allPositionSet.size() * 2);
        // 查询父岗位下的子岗位
        parentPositionSet.forEach(parentPosition -> this.getChildren(parentPosition, childrenPositionSet, allPositionSet));
        // 排除掉父岗位与子岗位重复的部分，防止树形结构出现重复
        parentPositionSet.removeAll(childrenPositionSet);
        // 把父岗位set和子岗位set重组为树形结构，返回给前端
        ArrayList<IapPositionTDto> childrenPositionList = new ArrayList<>(childrenPositionSet);
        parentPositionSet.forEach(parentPosition -> this.recursiveMenu(parentPosition, childrenPositionList));

        return parentPositionSet.stream().sorted(Comparator.comparing(IapPositionTDto::getCreateDate).reversed()).collect(Collectors.toList());
    }

    /**
     * 通过父递归查找子
     *
     * @param parentPosition      父岗位
     * @param childrenPositionSet 子岗位集合
     * @param allPositionSet      所有岗位集合
     */
    private void getChildren(IapPositionTDto parentPosition, Set<IapPositionTDto> childrenPositionSet, List<IapPositionTDto> allPositionSet) {
        // 筛选出 parentPosition 的子岗位
        Set<IapPositionTDto> collect = allPositionSet.stream()
                .filter(x -> x.getParentId() != null && x.getParentId().equals(parentPosition.getId())).collect(Collectors.toSet());
        if (collect != null && !collect.isEmpty()) {
            childrenPositionSet.addAll(collect);
            collect.stream().forEach(x -> this.getChildren(x, childrenPositionSet, allPositionSet));
        }
    }

    /**
     * 验证新增参数
     *
     * @param iapPositionT
     */
    private void verifySaveParam(IapPositionT iapPositionT) {
        if (StringUtils.isEmpty(iapPositionT.getCode())
                || iapSysPositionMapper.selectCount(
                new QueryWrapper<IapPositionT>().eq("code", iapPositionT.getCode())) != 0) {
            throw new RuntimeException();
        }
    }

    /**
     * @param obj  需要设置code属性的实体类
     * @param date 当前时间
     * @param num  默认0 大于 20跳出
     */
    private void setCode(IapPositionT obj, Date date, Integer num) throws CommonException {
        obj.setCode(CodeUtils.dateToCode("NX", date));
        if (num > CodeUtils.NUM) {
            log.error(CommonExceptionDefinition.getCurrentClassError() + "编码设置失败!");
            throw new CommonException(new CommonExceptionDefinition(500, "编码设置失败!"));
        }
        if (iapSysPositionMapper.selectCount(
                new QueryWrapper<IapPositionT>()
                        .ne("id", obj.getId())
                        .eq("code", obj.getCode())) != 0) {
            num++;
            this.setCode(obj, date, num);
        }
    }
}

