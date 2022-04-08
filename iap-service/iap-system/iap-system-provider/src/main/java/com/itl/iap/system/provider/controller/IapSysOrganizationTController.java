package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysOrganizationTDto;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import com.itl.iap.system.api.entity.excelDataEntity.SysOrganizationExcelEntity;
import com.itl.iap.system.api.service.IapSysOrganizationTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 组织Controller
 *
 * @author 李骐光
 * @date 2020-06-19
 * @since jdk1.8
 */
@Slf4j
@Api("System-组织结构管理控制层")
@RestController
@RequestMapping("/organization")
public class IapSysOrganizationTController {
    @Autowired
    private IapSysOrganizationTService organizationService;

    @PostMapping("/insertOrganization")
    @ApiOperation(value = "新增组织", notes = "新增组织")
    public ResponseData<String> insertOrganization(@RequestBody IapSysOrganizationTDto organizationDTO) throws CommonException {
        return ResponseData.success(organizationService.insertOrganization(organizationDTO));
    }

    @PostMapping("/updateOrganization")
    @ApiOperation(value = "修改组织", notes = "修改组织")
    public ResponseData<String> updateOrganization(@RequestBody IapSysOrganizationTDto organizationDTO) throws CommonException {
        return ResponseData.success(organizationService.updateOrganization(organizationDTO));
    }

    @PostMapping("/queryById")
    @ApiOperation(value = "通过parentOrgId查询", notes = "通过parentOrgId查询")
    public ResponseData<List<IapSysOrganizationTDto>> queryById(@RequestBody IapSysOrganizationTDto organizationDTO) {
        return ResponseData.success(organizationService.queryById(organizationDTO));
    }

    @PostMapping("/deleteById")
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    public ResponseData<Boolean> deleteById(@RequestBody IapSysOrganizationTDto organizationDTO) {
        return ResponseData.success(organizationService.deleteById(organizationDTO));
    }

    @PostMapping("/queryByOrgName")
    @ApiOperation(value = "通过组织名称查询", notes = "通过组织名称查询")
    public ResponseData<List<IapSysOrganizationTDto>> queryByOrgName(@RequestBody IapSysOrganizationTDto organizationDTO) {
        return ResponseData.success(organizationService.queryByOrgName(organizationDTO));
    }

    @PostMapping("/pageQuery")
    @ApiOperation(value = "获取组织结构的平面数据", notes = "获取组织结构的平面数据")
    public ResponseData<List<SysOrganizationExcelEntity>> pageQuery(@RequestBody IapSysOrganizationTDto sysOrganizationDto){
        return ResponseData.success(organizationService.pageQuery(sysOrganizationDto));
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量插入组织结构列表",notes = "批量插入组织结构列表")
    ResponseData<Boolean> saveBatch(@RequestBody List<IapSysOrganizationT> sysOrganizationList){
        if (sysOrganizationList.size() > 0){
            organizationService.saveBatch(sysOrganizationList);
            return ResponseData.success(true);
        }
        return ResponseData.success(false);
    }

    @GetMapping("/queryOrgPositionUserTreeList")
    @ApiOperation(value = "查询组织机构岗位下的用户", notes = "查询组织机构岗位下的用户")
    public ResponseData<List<Map>> queryOrgPositionUserTreeList(@RequestParam(value = "userOrPositOrOrganName",required = false) String userOrPositOrOrganName) {
        List<Map> sysOrganizationTreeList = organizationService.queryUserAndPositName(userOrPositOrOrganName);
        return ResponseData.success(sysOrganizationTreeList);
    }
}
