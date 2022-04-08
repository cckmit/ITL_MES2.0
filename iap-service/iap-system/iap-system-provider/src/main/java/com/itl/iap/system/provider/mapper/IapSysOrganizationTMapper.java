package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.dto.IapSysOrganizationTDto;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import com.itl.iap.system.api.entity.excelDataEntity.SysOrganizationExcelEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 组织mapper
 *
 * @author 李骐光
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysOrganizationTMapper extends BaseMapper<IapSysOrganizationT> {
    /**
     * 根据id查询
     *
     * @param organizationDTO 组织对象
     * @return List<IapSysOrganizationTDto>
     */
    List<IapSysOrganizationTDto> queryById(@Param("organizationDTO") IapSysOrganizationTDto organizationDTO);

    /**
     * 批量保存
     *
     * @param organizationList 组织对象集合
     * @return Integer
     */
    Integer insertListFromExcel(@Param("list") Collection<IapSysOrganizationT> organizationList);

    /**
     * 查询组织的平面结构列表
     *
     * @param organizationTDto 组织对象
     * @return List<SysOrganizationExcelEntity>
     */
    List<SysOrganizationExcelEntity> pageQuery(@Param("organizationDTO") IapSysOrganizationTDto organizationTDto);

    /**
     * 查询所有部门
     *
     * @param organizationName 部门名称
     * @return List<IapSysOrganizationTDto>
     */
    List<IapSysOrganizationTDto> queryByPositAndOrgan(@Param("organizationName") String organizationName);
}
