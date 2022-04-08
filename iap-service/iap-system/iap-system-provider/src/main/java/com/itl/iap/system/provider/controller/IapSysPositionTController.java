package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import com.itl.iap.system.api.dto.IapPositionTDto;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.service.IapPositionTService;
import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 岗位Controller
 *
 * @author 马家伦
 * @date 2020-06-16
 * @since jdk1.8
 */
@Api("System-岗位管理控制层")
@RestController
@RequestMapping("/iapPositionT")
public class IapSysPositionTController {

    @Resource
    IapPositionTService iapPositionService;

    @PostMapping("/add")
    @ApiOperation(value = "添加新岗位",notes = "添加新岗位")
    public ResponseData<String> add(@RequestBody  @Validated({ValidationGroupAdd.class}) IapPositionTDto iapPositionTto) throws CommonException {
        return ResponseData.success(iapPositionService.add(iapPositionTto));
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新岗位",notes = "更新岗位")
    public ResponseData<Boolean> update(@RequestBody @Validated({ValidationGroupUpdate.class}) IapPositionTDto iapPositionDto) throws CommonException {
        return ResponseData.success(iapPositionService.update(iapPositionDto));
    }

    @PostMapping("/query")
    @ApiOperation(value = "查询岗位",notes = "查询岗位")
    public ResponseData<IPage<IapPositionTDto>> query(@RequestBody IapPositionTDto iapPositionDto){
        return ResponseData.success(iapPositionService.query(iapPositionDto));
    }

    @PostMapping("/updateEnabled/{enabled}")
    @ApiOperation(value = "更新启用/禁用状态",notes = "更新启用/禁用状态")
    public ResponseData<Boolean> updateEnabled(@RequestBody IapPositionTDto iapPositionDto, @PathVariable Short enabled){
        return ResponseData.success(iapPositionService.updateEnabled(iapPositionDto,enabled));
    }

    @PostMapping("/getUserList")
    @ApiOperation(value = "通过岗位id查询用户列表",notes = "通过岗位id查询用户列表")
    public ResponseData<IPage<IapSysUserTDto>> getUserListByPositionId(@RequestBody IapPositionTDto iapPositionDto){
        return ResponseData.success(iapPositionService.queryUserListByPositionId(iapPositionDto));
    }

    @PostMapping("/deleteByPositionId")
    @ApiOperation(value = "通过岗位id删除",notes = "通过岗位id删除")
    public ResponseData<Boolean> deleteByPositionId(@RequestBody IapPositionTDto iapPositionDto){
        return ResponseData.success(iapPositionService.deleteById(iapPositionDto));
    }

    @PostMapping("/getListTree")
    @ApiOperation(value = "岗位树（分页）",notes = "岗位树（分页）")
    public ResponseData<IPage<IapPositionTDto>> getListTree(@RequestBody IapPositionTDto iapPositionDto){
        return ResponseData.success(iapPositionService.getListTree(iapPositionDto));
    }

    @PostMapping("/getTree")
    @ApiOperation(value = "岗位树（不分页）",notes = "岗位树（不分页）")
    public ResponseData<List<IapPositionTDto>> getTree(@RequestBody IapPositionTDto iapPositionDto){
        return ResponseData.success(iapPositionService.getTree(iapPositionDto));
    }

    @PostMapping("/getPositionByOrganizationId")
    @ApiOperation(value = "通过组织id查看该组织下的岗位树列表",notes = "通过组织id查看该组织下的岗位树列表")
    public ResponseData<List<IapPositionTDto>> getListTreeByOrgId(@RequestBody IapPositionTDto iapPositionDto){
        return ResponseData.success(iapPositionService.getListTreeByOrgId(iapPositionDto));
    }

    @GetMapping("/queryUserListByPositionId/{positionId}")
    @ApiOperation(value = "通过角色id查询所有用户列表",notes = "通过角色id查询所有用户列表")
    public ResponseData queryUserListByPositionId(@PathVariable("positionId") String positionId){
        return ResponseData.success(iapPositionService.queryUserListByPositionId(positionId));
    }

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除岗位",notes = "批量删除岗位")
    public ResponseData<Boolean> deleteBatch(@RequestBody List<IapPositionTDto> positionDtoList){
        return ResponseData.success(iapPositionService.deleteBatch(positionDtoList));
    }


}
