package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysAuthTDto;
import com.itl.iap.system.api.entity.IapSysAuthT;
import com.itl.iap.system.api.service.IapSysAuthTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限控制Controller
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Api("System-权限管理控制层")
@RestController
@RequestMapping("/iapSysAuthT")
public class IapSysAuthTController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IapSysAuthTService iapSysAuthService;

    @PostMapping("/add")
    @ApiOperation(value = "新增权限表记录", notes = "新增权限表记录")
    public ResponseData add(@RequestBody IapSysAuthT iapSysAuthT) throws CommonException {
        logger.info("IapSysAuthTDto add Record...");
        return ResponseData.success(iapSysAuthService.addAuth(iapSysAuthT));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "批量删除权限", notes = "批量删除权限")
    public ResponseData delete(@RequestBody List<IapSysAuthT> sysAuthList) {
        logger.info("IapSysAuthTDto delete Record...");
        return ResponseData.success(iapSysAuthService.deleteBatch(sysAuthList));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据权限ID修改权限表记录", notes = "根据权限ID修改权限表记录")
    public ResponseData update(@RequestBody IapSysAuthT iapSysAuthT) throws CommonException {
        logger.info("IapSysAuthTDto updateRecord...");
        return ResponseData.success(iapSysAuthService.updateAuth(iapSysAuthT));
    }

    @PostMapping("/queryPage")
    @ApiOperation(value = "查询权限", notes = "查询权限")
    public ResponseData<IPage<IapSysAuthTDto>> queryPage(@RequestBody IapSysAuthTDto iapSysAuthDto) {
        return ResponseData.success(iapSysAuthService.pageQuery(iapSysAuthDto));
    }

    @PostMapping("/getTree")
    @ApiOperation(value = "查询权限树", notes = "查询权限树")
    public ResponseData getTree(@RequestBody IapSysAuthTDto iapSysAuthDto) {
        return ResponseData.success(iapSysAuthService.getTree(iapSysAuthDto));
    }
    @PostMapping("/getTreeByState")
    @ApiOperation(value = "查询权限树ByState", notes = "查询权限树ByState")
    public ResponseData getTreeByState(@RequestBody IapSysAuthTDto iapSysAuthDto) {
        return ResponseData.success(iapSysAuthService.getTreeByState(iapSysAuthDto));
    }
}
