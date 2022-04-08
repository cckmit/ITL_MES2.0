package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.api.dto.IapSysOrganizationTDto;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import com.itl.iap.system.api.entity.excelDataEntity.SysOrganizationExcelEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 组织service
 *
 * @author 李骐光
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysOrganizationTService extends IService<IapSysOrganizationT> {
    /**
     * 新增Organization
     *
     * @param organizationDTO 组织对象
     * @return String
     */
    String insertOrganization(IapSysOrganizationTDto organizationDTO) throws CommonException;

    /**
     * 修改Organization
     *
     * @param organizationDTO 组织对象
     * @return String
     */
    String updateOrganization(IapSysOrganizationTDto organizationDTO) throws CommonException;

    /**
     * 通过parentOrgId查询
     *
     * @param organizationDTO 组织对象
     * @return List<IapSysOrganizationTDto>
     */
    List<IapSysOrganizationTDto> queryById(IapSysOrganizationTDto organizationDTO);

    /**
     * 通过id删除
     *
     * @param organizationDTO 组织对象
     * @return Boolean
     */
    Boolean deleteById(IapSysOrganizationTDto organizationDTO);

    /**
     * 通过组织名称查询组织树
     *
     * @param organizationDTO 组织对象
     * @return List<IapSysOrganizationTDto>
     */
    List<IapSysOrganizationTDto> queryByOrgName(IapSysOrganizationTDto organizationDTO);

    /**
     * 查询组织列表，平面结构
     *
     * @param organizationDto 组织对象
     * @return List<SysOrganizationExcelEntity>
     */
    List<SysOrganizationExcelEntity> pageQuery(IapSysOrganizationTDto organizationDto);

    /**
     * 通过父递归查找子
     *
     * @param parentOrganization 父组织
     * @param childrenOrgSet     子组织
     * @param allOrgDtoList      所有组织
     */
    void getChildren(IapSysOrganizationTDto parentOrganization, Set<IapSysOrganizationTDto> childrenOrgSet, List<IapSysOrganizationTDto> allOrgDtoList);

    /**
     * 方法功能描述：查询组织机构岗位下的用户
     *
     * @param userOrPositOrOrganName 用户或者组织或者岗位名称
     * @return List<Map>
     */
    List<Map> queryUserAndPositName(String userOrPositOrOrganName);

}
