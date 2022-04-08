package com.itl.iap.attachment.client.service;

import com.itl.iap.attachment.client.config.FallBackConfig;
import com.itl.iap.attachment.client.service.impl.OrganizationServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysOrganizationTDto;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import com.itl.iap.system.api.entity.excelDataEntity.SysOrganizationExcelEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 组织服务远程调用
 *
 * @author 马家伦
 * @date 2020-8-25
 * @since jdk 1.8
 */
@FeignClient(value = "iap-system-provider", fallback = OrganizationServiceImpl.class, configuration = FallBackConfig.class)
public interface OrganizationService {

    @PostMapping("/organization/pageQuery")
    @ApiOperation(value = "组织架构平面列表", notes = "组织架构平面列表")
    ResponseData<List<SysOrganizationExcelEntity>> pageQuery(IapSysOrganizationTDto sysOrganizationDto);

    @PostMapping("/organization/saveBatch")
    @ApiOperation(value = "批量插入组织结构列表",notes = "批量插入组织结构列表")
    ResponseData<Boolean> saveBatch(List<IapSysOrganizationT> sysOrganizationList);

    @GetMapping("/organization/queryOrgPositionUserTreeList")
    @ApiOperation(value = "查询组织机构岗位下的用户",notes = "查询组织机构岗位下的用户")
    ResponseData<List<Map>> queryOrgPositionUserTreeList(@RequestParam(value = "userOrPositOrOrganName",required = false) String userOrPositOrOrganName);

    @PostMapping("/organization/queryOrgPositionUserByUserName")
    @ApiOperation(value = "模糊查询机构下岗位下的用户",notes = "模糊查询机构下岗位下的用户")
    ResponseData<List<IapSysOrganizationTDto>> queryOrgPositionUserByUserName(IapSysOrganizationTDto sysOrganizationDto);
}
