package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapChangeDetailLogTDto;
import com.itl.iap.system.api.entity.IapChangeDetailLogT;
import com.itl.iap.system.api.service.IapChangeDetailLogTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作变动明细日志(IapChangeDetailLogT)表控制层
 *
 * @author linjs
 * @since 2020-10-30 11:12:21
 */
@Api(tags = "操作变动明细日志表控制层" )
@RestController
@RequestMapping("/iapChangeDetailLogT")
public class IapChangeDetailLogTController extends BaseController {

    @Autowired
    private IapChangeDetailLogTService iapChangeDetailLogService;
    
    @PostMapping("/add")
    @ApiOperation(value = "操作变动明细日志新增记录", notes = "操作变动明细日志新增记录")
    public ResponseData add(@RequestBody IapChangeDetailLogT iapChangeDetailLog) {
        return ResponseData.success(iapChangeDetailLogService.saveOrUpdate(iapChangeDetailLog));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "根据操作变动明细日志ID删除记录", notes = "根据操作变动明细日志ID删除记录")
    public ResponseData delete(@RequestBody IapChangeDetailLogT iapChangeDetailLog) {
        return ResponseData.success(iapChangeDetailLogService.removeById(iapChangeDetailLog.getId()));
    }
    
    @PutMapping("/update")
    @ApiOperation(value = "根据操作变动明细日志ID修改记录", notes = "根据操作变动明细日志ID修改记录")
    public ResponseData update(@RequestBody IapChangeDetailLogT iapChangeDetailLog) {
        return ResponseData.success(iapChangeDetailLogService.updateById(iapChangeDetailLog));
    }
    
    @PostMapping("/query")
    @ApiOperation(value = "分页查询操作变动明细日志", notes = "分页查询操作变动明细日志")
    public ResponseData queryRecord(@RequestBody IapChangeDetailLogTDto iapChangeDetailLogDto) {
        return ResponseData.success(iapChangeDetailLogService.pageQuery(iapChangeDetailLogDto));
    }
}