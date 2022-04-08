package com.itl.iap.system.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.util.CodeUtils;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapPositionTDto;
import com.itl.iap.system.api.dto.IapSysOrganizationTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import com.itl.iap.system.api.entity.excelDataEntity.SysOrganizationExcelEntity;
import com.itl.iap.system.api.service.IapSysOrganizationTService;
import com.itl.iap.system.provider.mapper.IapSysOrganizationTMapper;
import com.itl.iap.system.provider.mapper.IapSysPositionTMapper;
import com.itl.iap.system.provider.mapper.IapSysUserTMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织实现类
 *
 * @author 李骐光
 * @date 2020-06-19
 * @since jdk1.8
 */
@Slf4j
@Service
public class IapSysOrganizationTServiceImpl extends ServiceImpl<IapSysOrganizationTMapper, IapSysOrganizationT> implements IapSysOrganizationTService {

    @Resource
    private IapSysOrganizationTMapper organizationMapper;
    @Resource
    private IapSysPositionTMapper positionMapper;
    @Resource
    private IapSysUserTMapper iapSysUserMapper;

    /**
     * 新增Organization
     *
     * @param organizationDTO 组织对象
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String insertOrganization(IapSysOrganizationTDto organizationDTO) throws CommonException {

        IapSysOrganizationT organization = new IapSysOrganizationT();
        organizationDTO.setId(UUID.uuid32());
        if (organizationDTO.getCode() == null || organizationDTO.getCode().isEmpty()) {
            this.setCode(organizationDTO, new Date(), 0);
        } else {
            if (organizationMapper.selectCount(new QueryWrapper<IapSysOrganizationT>().eq("code", organizationDTO.getCode())) > 0) {
                throw new CommonException("输入的组织编码重复，请重新输入", 500);
            }
        }
        BeanUtils.copyProperties(organizationDTO, organization);
        organizationMapper.insert(organization);
        return organizationDTO.getId();
    }

    /**
     * 修改Organization
     *
     * @param organizationDTO 组织对象
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateOrganization(IapSysOrganizationTDto organizationDTO) throws CommonException {
        IapSysOrganizationT organization = DtoUtils.convertObj(organizationDTO, IapSysOrganizationT.class);
        // 根据组织名称查询数据库中的实体
        IapSysOrganizationT organizationInDb = organizationMapper.selectOne(new QueryWrapper<IapSysOrganizationT>().eq("code", organization.getCode()));
        // 如果数据库中的实体与前端传入的实体不一致，则表明前端修改的组织编码已经存在
        if (organizationInDb != null && organization.getId() != null && !organization.getId().equals(organizationInDb.getId())) {
            throw new CommonException("修改的组织编码已存在，请重新输入", 500);
        }
        organizationMapper.updateById(organization);
        return organizationDTO.getId();
    }

    /**
     * 通过parentOrgId查询
     *
     * @param organizationDTO 组织对象
     * @return List<IapSysOrganizationTDto>
     */
    @Override
    public List<IapSysOrganizationTDto> queryById(IapSysOrganizationTDto organizationDTO) {
        List<IapSysOrganizationTDto> organizationDTOList = organizationMapper.queryById(organizationDTO);
        List<IapSysOrganizationTDto> collect = organizationMapper.queryById(organizationDTO).stream().filter(x -> x.getParentOrgId() == null).collect(Collectors.toList());
        if (collect != null && !collect.isEmpty()) {
            for (IapSysOrganizationTDto dto : collect) {
                this.getByOrganization(dto, organizationDTOList);
            }
            return collect;
        } else {
            return organizationDTOList;
        }
    }

    /**
     * 递归查询
     *
     * @param parentOrganizationDTO
     */
    private void getByOrganization(IapSysOrganizationTDto dto, List<IapSysOrganizationTDto> parentOrganizationDTO) {
        List<IapSysOrganizationTDto> codeTreeList = new ArrayList<>();
        parentOrganizationDTO.forEach(x -> {
            if (x.getParentOrgId() != null && x.getParentOrgId().equals(dto.getId())) {
                codeTreeList.add(x);
                getByOrganization(x, parentOrganizationDTO);
            }
        });
        if (codeTreeList.size() > 0) {
            dto.setDtoList(codeTreeList);
        }
    }

    /**
     * 通过id删除
     *
     * @param organizationDTO 组织对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(IapSysOrganizationTDto organizationDTO) {
        int result = organizationMapper.deleteById(organizationDTO.getId());
        return result > 0;
    }

    /**
     * 通过组织名称查询组织树
     *
     * @param organizationDTO 组织对象
     * @return List<IapSysOrganizationTDto>
     */
    @Override
    public List<IapSysOrganizationTDto> queryByOrgName(IapSysOrganizationTDto organizationDTO) {

        // 先通过前端传入的条件查询
        Set<IapSysOrganizationTDto> parentOrgSet = new HashSet<>(organizationMapper.queryById(organizationDTO));
        if (parentOrgSet.isEmpty()){
            return new ArrayList<>(parentOrgSet);
        }
        // 全部组织列表
        List<IapSysOrganizationTDto> allOrgDtoList = DtoUtils.convertList(IapSysOrganizationTDto.class, this.list(new QueryWrapper<>()));
        // 创建一个子组织Set
        Set<IapSysOrganizationTDto> childrenOrgSet = new HashSet<>(allOrgDtoList.size() * 2);
        // 查询父组织下的子组织
        parentOrgSet.forEach(parentOrg -> this.getChildren(parentOrg, childrenOrgSet, allOrgDtoList));
        // 排除掉父组织与子组织重复部分，防止树状结构出现重复
        parentOrgSet.removeAll(childrenOrgSet);
        // 把父组织set和子组织set重组为树形结构，返回给前端
        ArrayList<IapSysOrganizationTDto> childrenOrgList = new ArrayList<>(childrenOrgSet);
        parentOrgSet.forEach(parentOrg -> this.getByOrganization(parentOrg, childrenOrgList));
        return parentOrgSet.stream().sorted(Comparator.comparing(IapSysOrganizationTDto::getCreateDate).reversed()).collect(Collectors.toList());

    }

    /**
     * 查询组织列表，平面结构
     *
     * @param organizationDto 组织对象
     * @return List<SysOrganizationExcelEntity>
     */
    @Override
    public List<SysOrganizationExcelEntity> pageQuery(IapSysOrganizationTDto organizationDto) {
        return organizationMapper.pageQuery(organizationDto);
    }

    /**
     * 通过父递归查找子
     *
     * @param parentOrganization 父组织
     * @param childrenOrgSet     子组织
     * @param allOrgDtoList      所有组织
     */
    @Override
    public void getChildren(IapSysOrganizationTDto parentOrganization, Set<IapSysOrganizationTDto> childrenOrgSet,
                            List<IapSysOrganizationTDto> allOrgDtoList) {
        // 筛选出 parentOrganization 的子组织
        Set<IapSysOrganizationTDto> collect = allOrgDtoList.stream()
                .filter(x -> x.getParentOrgId() != null && x.getParentOrgId().equals(parentOrganization.getId())).collect(Collectors.toSet());
        if (collect != null && !collect.isEmpty()) {
            childrenOrgSet.addAll(collect);
            collect.stream().forEach(x -> this.getChildren(x, childrenOrgSet, allOrgDtoList));
        }
    }

    /**
     * 批量保存
     *
     * @param entityList 组织对象集合
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<IapSysOrganizationT> entityList) {
        if (entityList != null && !entityList.isEmpty()) {
            organizationMapper.insertListFromExcel(entityList);
        }
        return false;
    }


    /**
     * @param obj  需要设置code属性的实体类
     * @param date
     * @param num  默认0 大于 20跳出
     * @return void
     * @Description: 设置编码值
     */
    private void setCode(IapSysOrganizationTDto obj, Date date, Integer num) throws CommonException {
        obj.setCode(CodeUtils.dateToCode("NX", date));
        if (num > CodeUtils.NUM) {
            log.error(CommonExceptionDefinition.getCurrentClassError() + "编码设置失败!");
            throw new CommonException(new CommonExceptionDefinition(500, "编码设置失败!"));
        }
        if (organizationMapper.selectCount(new QueryWrapper<IapSysOrganizationT>().ne("id", obj.getId()).eq("code", obj.getCode())) != 0) {
            num++;
            this.setCode(obj, date, num);
        }
    }

    /**
     * 方法功能描述：根据组织机构ID获取部门列表
     *
     * @param userOrPositOrOrganName 用户或者组织或者岗位名称
     * @return 返回  组织 岗位 或者用户
     */
    @Override
    public List<Map> queryUserAndPositName(String userOrPositOrOrganName) {
        // 获取所有用户
        List<IapSysUserTDto> iapSysUserDtos = iapSysUserMapper.queryUserInfoByUserName(userOrPositOrOrganName);
        // 获取没岗位的用户
        List<IapSysUserTDto> noPositUser = iapSysUserDtos.stream().filter(x -> StringUtils.isEmpty(x.getPositionId())).collect(Collectors.toList());
        // 获取树形结构岗位 并且用户已经放在岗位下面了
        List<IapPositionTDto> allPosition = this.forEachUserAndPosit(userOrPositOrOrganName, iapSysUserDtos);
        // 获取没有组织的岗位
        List<IapPositionTDto> noOrgPosit = allPosition.stream().filter(x -> x.getOrgId() != null).collect(Collectors.toList());
        // 获取组织
        List<IapSysOrganizationTDto> organizationDtoList = organizationMapper.queryByPositAndOrgan(userOrPositOrOrganName);
        // 获取所有父级组织
        List<IapSysOrganizationTDto> parentOrgan = organizationDtoList.stream().filter(x -> x.getParentOrgId() == null).collect(Collectors.toList());
        // 递归获取组织树
        if (!parentOrgan.isEmpty()) {
            for (IapSysOrganizationTDto iapSysOrganizationDto : parentOrgan) {
                this.recursiveOrg(iapSysOrganizationDto, organizationDtoList, allPosition);
            }
            // 放没有组织的岗位
            /*parentOrgan.get(parentOrgan.size() - 1)
                    .setPositionlist(noOrgPosit);*/
            // 放没有岗位的用户
         /*   parentOrgan.get(parentOrgan.size() - 1)
                    .setIapSysUserTDtos(noPositUser);*/
            List<Map> maps = new ArrayList();
            for (IapSysOrganizationTDto organizationDto : parentOrgan) {
                maps.add(JSONObject.parseObject(JSONObject.toJSONString(organizationDto), Map.class));
            }
            for (IapSysUserTDto userDto : noPositUser) {
                maps.add(JSONObject.parseObject(JSONObject.toJSONString(userDto), Map.class));
            }
            return maps;
        } else {
            // 判断岗位是否为null  不为null 直接返回
            if (!allPosition.isEmpty()) {
                List<Map> postMap = new ArrayList();
                for (IapPositionTDto positionDto : allPosition) {
                    postMap.add(JSONObject.parseObject(JSONObject.toJSONString(positionDto), Map.class));
                }
                return postMap;
            } else {
                // 判断用户是否为null  不为null直接返回
                if (!iapSysUserDtos.isEmpty()) {
                    List<Map> userMap = new ArrayList();
                    for (IapSysUserTDto iapSysUserDto : iapSysUserDtos) {
                        userMap.add(JSONObject.parseObject(JSONObject.toJSONString(iapSysUserDto), Map.class));
                    }
                    return userMap;
                }
            }
        }
        // 都没有 则返回空数组
        return new ArrayList();
    }

    /**
     * IM 岗位下面放用户
     *
     * @param userOrPositOrOrganName 用户或者组织或者岗位名称
     * @param iapSysUserDtos         用户集合
     * @return 返回树型结构的岗位
     */
    private List<IapPositionTDto> forEachUserAndPosit(String userOrPositOrOrganName, List<IapSysUserTDto> iapSysUserDtos) {
        List<IapSysUserTDto> userPosit = iapSysUserDtos.stream().filter(x -> StringUtils.isNotEmpty(x.getPositionId())).collect(Collectors.toList());
        List<IapPositionTDto> allPosition = positionMapper.getPositUser(userOrPositOrOrganName, IapPositionTDto.ENABLED0);
        //  处理岗位和数据直接的关联
        allPosition.stream().forEach(x -> {
            List<IapSysUserTDto> userDtos = new ArrayList();
            for (IapSysUserTDto iapSysUserDto : userPosit) {
                if (iapSysUserDto.getPositionId() != null && x.getId().equals(iapSysUserDto.getPositionId())) {
                    userDtos.add(iapSysUserDto);
                }
            }
            x.setIapSysUserTLists(userDtos);
        });
        // 获取所有父级岗位
        List<IapPositionTDto> parentPosition = allPosition.stream().filter(x -> StringUtils.isEmpty(x.getParentId())).collect(Collectors.toList());
        // 开始递归
        for (IapPositionTDto positionDto : parentPosition) {
            this.recursivePosit(positionDto, allPosition);
        }
        // 判断所有父级岗位是否为空  如果为空 就没有子集存在
        if (CollectionUtils.isEmpty(parentPosition)) {
            return allPosition;
        } else {
            return parentPosition;
        }
    }

    /**
     * IM递归岗位
     *
     * @param list          父级岗位
     * @param recursiveDtos 所有岗位
     */
    private void recursivePosit(IapPositionTDto list, List<IapPositionTDto> recursiveDtos) {
        List<IapPositionTDto> codeTreeList = new ArrayList<>();
        recursiveDtos.forEach(x -> {
            if (x.getParentId() != null && x.getParentId().equals(list.getId())) {
                codeTreeList.add(x);
                this.recursivePosit(x, recursiveDtos);
            }
        });
        list.setPositionlist(codeTreeList);
    }

    /**
     * IM递归组织
     *
     * @param iapSysOrganizationDto 父组织
     * @param recursiveDtos         所有组织
     * @param allPosition           所有已经组装好了的岗位
     */
    private void recursiveOrg(IapSysOrganizationTDto iapSysOrganizationDto, List<IapSysOrganizationTDto> recursiveDtos, List<IapPositionTDto> allPosition) {
        List<IapSysOrganizationTDto> codeTreeList = new ArrayList<>();
        recursiveDtos.forEach(x -> {
            List<IapPositionTDto> iapPositionList = new ArrayList<>();
            allPosition.forEach(y -> {
                if (y.getOrgId() != null && y.getOrgId().equals(x.getId())) {
                    iapPositionList.add(y);
                }
            });
            x.setPositionlist(iapPositionList);
            if (x.getParentOrgId() != null && x.getParentOrgId().equals(iapSysOrganizationDto.getId())) {
                codeTreeList.add(x);
                this.recursiveOrg(x, recursiveDtos, allPosition);
            }
        });
        iapSysOrganizationDto.setDtoList(codeTreeList);
    }
}
