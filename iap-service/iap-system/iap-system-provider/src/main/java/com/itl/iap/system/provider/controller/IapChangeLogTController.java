package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapChangeLogTDto;
import com.itl.iap.system.api.entity.IapChangeLogT;
import com.itl.iap.system.api.service.IapChangeLogTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作变动日志(IapChangeLogT)表控制层
 *
 * @author linjs
 * @since 2020-10-30 10:48:36
 */
@Api("操作变动日志表控制层" )
@RestController
@RequestMapping("/iapChangeLogT")
public class IapChangeLogTController extends BaseController {

    @Autowired
    private IapChangeLogTService iapChangeLogService;
    
    @PostMapping("/add")
    @ApiOperation(value = "新增操作变动日志", notes = "新增操作变动日志")
    public ResponseData add(@RequestBody IapChangeLogT iapChangeLog) {
        return ResponseData.success(iapChangeLogService.saveOrUpdate(iapChangeLog));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "根据操作变动日志ID删除记录", notes = "根据操作变动日志ID删除记录")
    public ResponseData delete(@RequestBody IapChangeLogT iapChangeLog) {
        return ResponseData.success(iapChangeLogService.removeById(iapChangeLog.getId()));
    }
    
    @PutMapping("/update")
    @ApiOperation(value = "根据操作变动日志ID修改记录", notes = "根据操作变动日志ID修改记录")
    public ResponseData update(@RequestBody IapChangeLogT iapChangeLog) {
        return ResponseData.success(iapChangeLogService.updateById(iapChangeLog));
    }
    
    @PostMapping("/query")
    @ApiOperation(value = "分页查询操作变动日志", notes = "分页查询操作变动日志")
    public ResponseData queryRecord(@RequestBody IapChangeLogTDto iapChangeLogDto) {
        return ResponseData.success(iapChangeLogService.pageQuery(iapChangeLogDto));
    }

    @PostMapping("/queryChangeDetailById")
    @ApiOperation(value = "根据操作变动ID查询操作变动明细", notes = "根据操作变动ID查询操作变动明细")
    public ResponseData queryChangeDetailById(@RequestBody IapChangeLogTDto iapChangeLogDto) {
        return ResponseData.success(iapChangeLogService.queryChangeDetailById(iapChangeLogDto));
    }
}