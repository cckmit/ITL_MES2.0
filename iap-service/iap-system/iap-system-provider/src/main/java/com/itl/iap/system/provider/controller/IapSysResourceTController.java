package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.entity.IapSysAuthResourceT;
import com.itl.iap.system.api.entity.IapSysResourceT;
import com.itl.iap.system.api.service.IapSysAuthResourceTService;
import com.itl.iap.system.api.service.IapSysResourceTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单Controller
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
@Api("System-菜单管理控制层")
@RestController
@RequestMapping("/iapSysResourceT")
public class IapSysResourceTController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IapSysResourceTService iapSysResourceService;
    @Autowired
    private IapSysAuthResourceTService authResourceService;

    @PostMapping("/add")
    @ApiOperation(value = "新增记录", notes = "新增记录")
    public ResponseData add(@RequestBody IapSysResourceT iapSysResourceT) {
        logger.info("IapSysResourceTDto add Record...");
        if (iapSysResourceT.getId() == null) {
            iapSysResourceT.setId(UUID.uuid32());
        }
        return ResponseData.success(iapSysResourceService.saveOrUpdate(iapSysResourceT));
    }

    @PostMapping("/deleteByIds")
    @ApiOperation(value = "根据ID删除记录", notes = "根据ID删除记录")
    public ResponseData delete(@RequestBody List<IapSysResourceTDto> iapSysResourceDto) {
        logger.info("IapSysResourceTDto delete Record...");
        return ResponseData.success(iapSysResourceService.deleteTree(iapSysResourceDto));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据ID修改记录", notes = "根据ID修改记录")
    public ResponseData update(@RequestBody IapSysResourceT iapSysResourceT) {
        logger.info("IapSysResourceTDto updateRecord...");
        return ResponseData.success(iapSysResourceService.updateById(iapSysResourceT));
    }

    @GetMapping("/queryList")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ResponseData queryRecord() {
        logger.info("IapSysResourceTDto queryRecord...");
        return ResponseData.success(iapSysResourceService.queryList());
    }

    @PostMapping("/addSourceAll/{authId}")
    @ApiOperation(value = "权限列表菜单添加", notes = "权限列表菜单添加")
    public ResponseData addSourceAll(@RequestBody List<IapSysAuthResourceT> authResourceTs, @PathVariable("authId") String authId) {
        logger.info("IapSysAuthResourceT saveBatch");
        return ResponseData.success(authResourceService.saveAuthResource(authResourceTs,authId));
    }

    @GetMapping("/selectSource")
    @ApiOperation(value = "权限列表菜单查询", notes = "权限列表菜单查询")
    public ResponseData selectSource(@RequestParam("authId") String authId) {
        logger.info("IapSysAuthResourceT selectSource");
        return ResponseData.success(authResourceService.list(new QueryWrapper<IapSysAuthResourceT>()
                .eq("auth_id", authId).and(war -> war.eq("del_flag", "0"))));
    }

    @GetMapping("/getAllMenu")
    @ApiOperation(value = "获取当前用户菜单", notes = "获取当前用户菜单")
    public ResponseData getAllMenu() {
        logger.info("IapSysAuthResourceT selectSource");
        return ResponseData.success(iapSysResourceService.getAllMenu());
    }

}