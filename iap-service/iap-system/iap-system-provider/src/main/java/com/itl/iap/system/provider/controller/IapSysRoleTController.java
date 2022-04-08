package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.controller.BaseController;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysRoleTDto;
import com.itl.iap.system.api.entity.IapSysRoleT;
import com.itl.iap.system.api.service.IapSysRoleTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色Controller
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Api("System-角色管理控制层")
@RestController
@RequestMapping("/iapSysRoleT")
public class IapSysRoleTController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IapSysRoleTService iapSysRoleService;

    @PostMapping("/add")
    @ApiOperation(value = "新增角色表记录", notes = "新增角色表记录")
    public ResponseData add(@RequestBody IapSysRoleT iapSysRoleT) throws CommonException {
        logger.info("IapSysRoleTDto add Record...");

        return ResponseData.success(iapSysRoleService.add(iapSysRoleT));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据角色ID删除角色表记录", notes = "根据角色ID删除角色表记录")
    public ResponseData delete(@RequestBody IapSysRoleT iapSysRoleT) {
        logger.info("IapSysRoleTDto delete Record...");
        return ResponseData.success(iapSysRoleService.removeRoleById(iapSysRoleT.getId()));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据角色ID修改角色表记录", notes = "根据角色ID修改角色表记录")
    public ResponseData update(@RequestBody IapSysRoleT iapSysRoleT) throws CommonException {
        logger.info("IapSysRoleTDto updateRecord...");
        return ResponseData.success(iapSysRoleService.updateRole(iapSysRoleT));
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询角色表", notes = "分页查询角色表")
    public ResponseData queryRecord(@RequestBody IapSysRoleTDto iapSysRoleDto) {
        logger.info("IapSysRoleTDto queryRecord...");
        return ResponseData.success(iapSysRoleService.pageQuery(iapSysRoleDto));
    }
    @PostMapping("/queryByState")
    @ApiOperation(value = "分页查询角色表ByState", notes = "分页查询角色表ByState")
    public ResponseData queryRecordByState(@RequestBody IapSysRoleTDto iapSysRoleDto) {
        logger.info("IapSysRoleTDto queryRecord...");
        return ResponseData.success(iapSysRoleService.pageQueryByState(iapSysRoleDto));
    }

    @PostMapping("/queryUserListByRoleId")
    @ApiOperation(value = "通过角色id查询用户列表", notes = "通过角色id查询用户列表")
    public ResponseData queryUserListByRoleId(@RequestBody IapSysRoleTDto iapSysRoleDto) {
        return ResponseData.success(iapSysRoleService.queryUserListByRoleId(iapSysRoleDto));
    }

    @GetMapping("/queryAllUserListByRoleId/{roleId}")
    @ApiOperation(value = "通过角色id查询所有用户列表", notes = "通过角色id查询所有用户列表")
    public ResponseData queryAllUserListByRoleId(@PathVariable("roleId") String roleId) {
        return ResponseData.success(iapSysRoleService.queryAllUserListByRoleId(roleId));
    }

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除角色", notes = "批量删除角色")
    public ResponseData deleteBatch(@RequestBody List<IapSysRoleT> sysRoleList) {
        logger.info("IapSysRoleTDto deleteBatch Record...");
        return ResponseData.success(iapSysRoleService.deleteBatch(sysRoleList));
    }


}
