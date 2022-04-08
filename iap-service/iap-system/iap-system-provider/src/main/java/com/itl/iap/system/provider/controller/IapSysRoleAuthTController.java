package com.itl.iap.system.provider.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.controller.BaseController;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysRoleAuthTDto;
import com.itl.iap.system.api.entity.IapSysRoleAuthT;
import com.itl.iap.system.api.service.IapSysRoleAuthTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限中间表Controller
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Api("System-角色权限中间表控制层")
@RestController
@RequestMapping("/iapSysRoleAuthT")
public class IapSysRoleAuthTController extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private IapSysRoleAuthTService iapSysRoleAuthService;
    
    @PostMapping("/batchRemoveAndAdd")
    @ApiOperation(value = "批删批增记录", notes = "批删批增记录")
    public ResponseData batchRemoveAndAdd(@RequestBody List<IapSysRoleAuthT> iapSysRoleAuthT) {
        logger.info("IapSysRoleAuthTDto batchRemoveAndAdd Record...");
        return ResponseData.success(iapSysRoleAuthService.batchRemoveAndAdd(iapSysRoleAuthT));
    }
    @PostMapping("/queryAllByRoleId")
    @ApiOperation(value = "查询某个角色的所有权限", notes = "查询某个角色的所有权限")
    public ResponseData queryAllByRoleId(@RequestBody IapSysRoleAuthT iapSysRoleAuthT) {
        logger.info("IapSysRoleAuthTDto queryAll Record...");
        return ResponseData.success(iapSysRoleAuthService.list(new QueryWrapper<IapSysRoleAuthT>().eq("role_id", iapSysRoleAuthT.getRoleId())));
    }
    @PostMapping("/add")
    @ApiOperation(value = "新增记录", notes = "新增记录")
    public ResponseData add(@RequestBody IapSysRoleAuthT iapSysRoleAuthT) {
        logger.info("IapSysRoleAuthTDto add Record...");
        return ResponseData.success(iapSysRoleAuthService.saveOrUpdate(iapSysRoleAuthT));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据ID删除记录", notes = "根据ID删除记录")
    public ResponseData delete(@RequestBody IapSysRoleAuthT iapSysRoleAuthT) {
        logger.info("IapSysRoleAuthTDto delete Record...");
        return ResponseData.success(iapSysRoleAuthService.removeById(iapSysRoleAuthT.getId()));
    }
    
    @PostMapping("/update")
    @ApiOperation(value = "根据ID修改记录", notes = "根据ID修改记录")
    public ResponseData update(@RequestBody IapSysRoleAuthT iapSysRoleAuthT) {
        logger.info("IapSysRoleAuthTDto updateRecord...");
        return ResponseData.success(iapSysRoleAuthService.updateById(iapSysRoleAuthT));
    }
    
    @PostMapping("/query")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ResponseData queryRecord(@RequestBody IapSysRoleAuthTDto iapSysRoleAuthDto) {
        logger.info("IapSysRoleAuthTDto queryRecord...");
        return ResponseData.success(iapSysRoleAuthService.pageQuery(iapSysRoleAuthDto));
    }
}