package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.SimLovDto;
import com.itl.iap.system.api.entity.SimLov;
import com.itl.iap.system.api.service.SimLovService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author EbenChan
 * @date 2020/11/21 18:30
 **/
@Api("System-SimLov控制器")
@RestController
@RequestMapping("/simLov")
public class SimLovController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimLovService simLovService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询SimLov表信息", notes = "分页查询SimLov表信息")
    public ResponseData queryRecord(@RequestBody SimLovDto simLovDto) {
        logger.info("SimLovDto queryRecord...");
        return ResponseData.success(simLovService.pageQuery(simLovDto));
    }

    @PutMapping("/add")
    @ApiOperation(value = "新增SimLov", notes = "新增SimLov")
    public ResponseData save(@RequestBody SimLov simLov) throws CommonException {
        logger.info("SimLovDto save...");
        return ResponseData.success(simLovService.insertLov(simLov));
    }
    @PutMapping("/update")
    @ApiOperation(value = "修改SimLov", notes = "修改SimLov")
    public ResponseData update(@RequestBody SimLov simLov) throws CommonException{
        logger.info("SimLovDto update...");
        return ResponseData.success(simLovService.updateLov(simLov));
    }
    @DeleteMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseData remove(@RequestBody List<String> ids) {
        logger.info("SimLovDto remove...");
        return ResponseData.success(simLovService.removeByIds(ids));
    }

    @GetMapping("/getByCode/{code}")
    @ApiOperation(value = "修改SimLov", notes = "修改SimLov")
    public ResponseData getByCode(@PathVariable String code){
        logger.info("SimLovDto getByCode...");
        return ResponseData.success(simLovService.getByCode(code));
    }
}
