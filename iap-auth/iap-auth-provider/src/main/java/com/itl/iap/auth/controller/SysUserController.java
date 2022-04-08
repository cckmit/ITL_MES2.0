package com.itl.iap.auth.controller;

import com.itl.iap.auth.dto.SysUserDto;
import com.itl.iap.auth.entity.SysUser;
import com.itl.iap.auth.service.ISysUserService;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IapSysUser表控制层
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:21
 * @since jdk1.8
 */

@Api("Auth-IapSysUser表控制层")
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private ISysUserService sysUserService;

    @PostMapping("/add")
    @ApiOperation(value = "新增用户记录", notes = "新增用户记录")
    public ResponseData add(@RequestBody SysUser sysUser) {
        logger.info("IapSysUserTDto add Record...");
        return ResponseData.success(sysUserService.saveOrUpdate(sysUser));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据用户ID删除用户记录", notes = "根据用户ID删除用户记录")
    public ResponseData delete(@RequestBody SysUser sysUser) {
        logger.info("IapSysUserTDto delete Record...");
        return ResponseData.success(sysUserService.removeById(sysUser.getId()));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据用户ID修改用户记录", notes = "根据ID修改记录")
    public ResponseData update(@RequestBody SysUser sysUser) {
        logger.info("IapSysUserTDto updateRecord...");
        return ResponseData.success(sysUserService.updateById(sysUser));
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询用户表", notes = "分页查询用户表")
    public ResponseData queryRecord(@RequestBody SysUserDto sysUserDto) {
        logger.info("IapSysUserTDto queryRecord...");
        return ResponseData.success(sysUserService.pageQuery(sysUserDto));
    }
}