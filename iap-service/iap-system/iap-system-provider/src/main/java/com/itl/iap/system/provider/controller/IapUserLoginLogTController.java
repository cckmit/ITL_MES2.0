package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.controller.BaseController;

import com.itl.iap.common.base.response.ResponseData;

import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.dto.IapUserLoginLogTDto;
import com.itl.iap.system.api.entity.IapUserLoginLogT;
import com.itl.iap.system.api.service.IapOpsLogTService;
import com.itl.iap.system.api.service.IapUserLoginLogTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户登录日志Controller
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Api("System-用户登录日志控制层")
@RestController
@RequestMapping("/iapUserLoginLogT")
public class IapUserLoginLogTController extends BaseController {

    @Resource
    private UserUtil userUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IapUserLoginLogTService iapUserLoginLogService;

    @Autowired
    private IapOpsLogTService iIapOpsLogService;

    @PostMapping("/add")
    @ApiOperation(value = "新增登陆记录", notes = "新增登陆记录")
    public ResponseData add(@RequestBody IapUserLoginLogT iapUserLoginLogT) {
        logger.info("IapUserLoginLogTDto add Record...");
        return ResponseData.success(iapUserLoginLogService.saveOrUpdate(iapUserLoginLogT));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据日志ID删除记录", notes = "根据日志ID删除记录")
    public ResponseData delete(@RequestBody IapUserLoginLogT iapUserLoginLogT) {
        logger.info("IapUserLoginLogTDto delete Record...");
        return ResponseData.success(iapUserLoginLogService.removeById(iapUserLoginLogT.getId()));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据日志ID修改记录", notes = "根据日志ID修改记录")
    public ResponseData update(@RequestBody IapUserLoginLogT iapUserLoginLogT) {
        logger.info("IapUserLoginLogTDto updateRecord...");
        return ResponseData.success(iapUserLoginLogService.updateById(iapUserLoginLogT));
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询登陆日志", notes = "分页查询登陆日志")
    public ResponseData queryRecord(@RequestBody IapUserLoginLogTDto iapUserLoginLogDto) {
        logger.info("IapUserLoginLogTDto queryRecord...");
        return ResponseData.success(iapUserLoginLogService.pageQuery(iapUserLoginLogDto));
    }

    @PostMapping("/queryOpsLog")
    @ApiOperation(value = "分页查询系统日志", notes = "分页查询系统日志")
    public ResponseData queryOpsLog(@RequestBody IapOpsLogTDto iapOpsLogDto) {
        logger.info("IapOpsLogTDto queryRecord...");
        return ResponseData.success(iIapOpsLogService.pageQuery(iapOpsLogDto));
    }

    @PostMapping("/queryPersonalLoginLog")
    @ApiOperation(value = "查询个人登陆日志分页", notes = "查询个人登陆日志分页")
    public ResponseData queryPersonalLoginLog(@RequestBody IapUserLoginLogTDto iapUserLoginLogDto) {
        logger.info("IapUserLoginLogTDto queryRecord...");
        iapUserLoginLogDto.setUserId(userUtil.getUser().getUserName());
        return ResponseData.success(iapUserLoginLogService.pageQuery(iapUserLoginLogDto));
    }

}