package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.SimLovDetailDto;
import com.itl.iap.system.api.entity.SimLovDetail;
import com.itl.iap.system.api.service.SimLovDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author EbenChan
 * @date 2020/11/21 21:21
 **/
@Api("System-SimLovDetail控制器")
@RestController
@RequestMapping("/simLovDetail")
public class SimLovDetailController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimLovDetailService simLovDetailService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询SimLov表信息", notes = "分页查询SimLov表信息")
    public ResponseData queryRecord(@RequestBody SimLovDetailDto simLovDetailDto) {
        logger.info("SimLovDto queryRecord...");
        return ResponseData.success(simLovDetailService.pageQuery(simLovDetailDto));
    }

    @PutMapping("/add")
    @ApiOperation(value = "新增SimLov", notes = "新增SimLov")
    public ResponseData save(@RequestBody SimLovDetail simLovDetail) throws CommonException {
        logger.info("SimLovDto save...");
        return ResponseData.success(simLovDetailService.insertLovDetail(simLovDetail));
    }
    @PutMapping("/update")
    @ApiOperation(value = "修改SimLov", notes = "修改SimLov")
    public ResponseData update(@RequestBody SimLovDetail simLovDetail) throws CommonException{
        logger.info("SimLovDto update...");
        return ResponseData.success(simLovDetailService.updateLovDetail(simLovDetail));
    }
    @DeleteMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseData remove(@RequestBody List<String> ids) {
        logger.info("SimLovDto remove...");
        return ResponseData.success(simLovDetailService.removeByIds(ids));
    }
}
